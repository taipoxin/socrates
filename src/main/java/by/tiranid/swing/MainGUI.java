package by.tiranid.swing;

import by.tiranid.swing.component.LogJFrame;
import by.tiranid.swing.component.LogJTextArea;
import by.tiranid.swing.listeners.LeftClickMouseListener;
import by.tiranid.sync.FileUtils;
import by.tiranid.timer.SimpleTimer;
import by.tiranid.timer.TimerUtils;
import by.tiranid.web.RequestSender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.List;


@Slf4j
public class MainGUI {

    @Getter
    private final int windowWidth = 300;
    @Getter
    private final int windowHeight = 200;
    @Getter
    private final String APPLICATION_NAME = "Socrates";
    @Getter
    private final String ICON_STR = "/images/32_32img.png";

    @Getter
    private long iterationTimeMs;
    private JFrame mainFrame;
    private JTextArea timerTextArea;
    private SimpleTimer simpleTimer;
    private long lastRemainSeconds = 0;
    private int iterationSeconds = 10;


    private JPanel setupJPanel() {
        JPanel panel = new JPanel();
        JTextArea textarea = new LogJTextArea("00:00:00");
        panel.add(textarea);
        timerTextArea = textarea;
        return panel;
    }

    private JFrame setupJFrame() {
        JFrame frame = new LogJFrame(APPLICATION_NAME);
        frame.setLocationRelativeTo(null);
        // hiding in tray
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel mainPane = setupJPanel();
        frame.setContentPane(mainPane);
        frame.setMinimumSize(new Dimension(windowWidth, windowHeight));
        frame.pack();
        frame.setVisible(false);
        return frame;
    }

    private void createGUI() {
        log.info("creating GUI in UI thread");
        mainFrame = setupJFrame();
    }

    private MenuItem createMenuItem(String label, ActionListener listener) {
        MenuItem item = new MenuItem(label);
        item.addActionListener(listener);
        return item;
    }

    private PopupMenu setupTrayMenu() {
        PopupMenu trayMenu = new PopupMenu();

        // show app
        trayMenu.add(createMenuItem("Show settings", (ActionEvent e) -> mainFrame.setVisible(true)));

        // run iter
        trayMenu.add(createMenuItem("Run iteration", (ActionEvent e) -> {
            iterationTimeMs = System.currentTimeMillis();
            timerTextArea.setText(TimerUtils.convertMillisToTime(iterationSeconds * 1000));
            simpleTimer = new SimpleTimer(iterationSeconds, 50, this);
            simpleTimer.startNewTimer();
        }));

        // close app
        trayMenu.add(createMenuItem("Exit", (ActionEvent e) -> System.exit(0)));

        return trayMenu;
    }


    /**
     * ad-hoc solution for for opening trayMenu on clicking left and right mouse buttons
     *
     * @param trayMenu trayMenu, that opened
     * @return full configurated listener
     */
    private MouseListener setupMouseListener(PopupMenu trayMenu) {
        // ad-hoc solution
        final Frame frame = new Frame("");
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setType(Window.Type.UTILITY);
        frame.setVisible(true);

        return new LeftClickMouseListener(frame, trayMenu);
    }

    private TrayIcon setupTrayIcon() {
        if (!SystemTray.isSupported()) {
            return null;
        }

        PopupMenu trayMenu = setupTrayMenu();

        URL imageURL = TrayIcon.class.getResource(ICON_STR);

        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        TrayIcon trayIcon = new TrayIcon(icon, APPLICATION_NAME);
        trayIcon.setPopupMenu(trayMenu);
        trayIcon.setImageAutoSize(true);

        // open menu by clicking
        trayIcon.addMouseListener(setupMouseListener(trayMenu));
        return trayIcon;
    }

    private void setupSystemTray() {
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(setupTrayIcon());
        } catch (AWTException e) {
            log.error("tray is not supported", e);
        } catch (NullPointerException e) {
            log.error("trouble with trayIcon", e);
        }
    }

    public void setupUI() {
        createGUI();
        setupSystemTray();
    }


    private void stopTimer() {
        if (!mainFrame.isVisible()) {
            mainFrame.setVisible(true);
        }
        simpleTimer.stop();
    }


    private void syncAndClean(String login) {
        boolean success = RequestSender.syncUserData(login, RequestSender.postIterationURI);
        // успешная доставка запросов
        if (success) {
            FileUtils.cleanAfterSync(login);
        }
    }


    /**
     * @param timerMillis запланированное время конца
     */
    public void updateTimerTextArea(long timerMillis) {
        // сколько осталось работать
        long millis = timerMillis - System.currentTimeMillis();

        if (millis > 0) {
            long remainSeconds = millis / 1000;
            // если поменялась секунда
            if (lastRemainSeconds != remainSeconds) {
                String hms = TimerUtils.convertMillisToTime(millis);
                log.info("{} ({} seconds) remaining", hms, remainSeconds);

                timerTextArea.setText(hms);
                lastRemainSeconds = remainSeconds;
            }
        } else if (!SimpleTimer.timerStopped) {
            SimpleTimer.timerStopped = true;
            log.info("time is up");
            log.info("stop timer");

            // stop all and notify that timer gone
            stopTimer();

            // sending request to spring
            log.info("send request");
            List<NameValuePair> record = RequestSender.createTimeRecord(iterationTimeMs);
            FileUtils.saveRecordToFile(record);

            boolean trigger = RequestSender.isGetConnectionTo(RequestSender.postIterationURI);
            if (trigger) {
                syncAndClean("login");
            }
        }
    }


}
