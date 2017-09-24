package by.tiranid.swing;

import by.tiranid.swing.component.LogJFrame;
import by.tiranid.swing.component.LogJTextArea;
import by.tiranid.swing.listeners.LeftClickMouseListener;
import by.tiranid.timer.SimpleTimer;
import by.tiranid.timer.TimerUtils;
import by.tiranid.web.RequestSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.net.URL;


public class TrayIconImpl {

    private static final Logger log = LoggerFactory.getLogger(TrayIconImpl.class);


    private final int windowWidth = 300;
    private final int windowHeight = 200;


    private final String APPLICATION_NAME = "Socrates";
    private final String ICON_STR = "/images/32_32img.png";
    public long iterationTimeMs;
    private JFrame mainFrame;
    private JTextArea timerTextArea;
    private SimpleTimer simpleTimer;
    private long lastS = 0;
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
            timerTextArea.setText(TimerUtils.convertMillisToTime(iterationSeconds * 1000));
            simpleTimer = new SimpleTimer(iterationSeconds, this);
            simpleTimer.count();
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


    private void timeIsUp() {
        if (!mainFrame.isVisible()) {
            mainFrame.setVisible(true);
        }
        simpleTimer.stop();
    }


    /**
     * @param timerMillis запланированное время конца
     */
    public void updateTextArea(long timerMillis) {
        // сколько осталось работать
        long millis = timerMillis - System.currentTimeMillis();

        if (millis > 0) {
            long sec = millis / 1000;
            if (lastS != sec) {
                String hms = TimerUtils.convertMillisToTime(millis);
                log.info("{} ({} seconds) remaining", hms, sec);
                timerTextArea.setText(hms);
                lastS = millis / 1000;
            }
        } else if (!SimpleTimer.timerStopped) {
            SimpleTimer.timerStopped = true;
            log.info("time is up");
            log.info("stop timer");

            // stop all and notify that timer gone
            timeIsUp();
            // sending request to spring
            log.info("send request");
            RequestSender.sendRequest(iterationTimeMs);
        }
    }


}
