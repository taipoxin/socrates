package by.tiranid.swing;

import by.tiranid.timer.SimpleTimer;
import by.tiranid.utils.MainUtils;
import by.tiranid.web.RequestSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class TrayIconImpl {

    public static final String APPLICATION_NAME = "Socrates";
    public static final String ICON_STR = "/images/32_32img.png";

    private static JFrame mainFrame;
    private static boolean appVisibility = false;
    private static JTextArea textarea;
    static long lastS = 0;

    private static int iterationSeconds = 10;

    public static long iterationTimeMs;

    private static SimpleTimer simpleTimer;


    private static final Logger log = Logger.getLogger(TrayIconImpl.class.getName());



    public static void main(String[] args) {
        log.info("Started main thread");
        MainUtils.loadDefaultProperties();


        SwingUtilities.invokeLater(TrayIconImpl::createGUI);
    }

    private static void createGUI() {
        mainFrame = new JFrame(APPLICATION_NAME);
        // hiding in tray
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel mainPane = new JPanel(); // создаём панель, на которой будет лежать текстовое поле
        textarea = new JTextArea();
        mainPane.add(textarea);
        mainFrame.setContentPane(mainPane);
        mainFrame.setMinimumSize(new Dimension(300, 200));

        mainFrame.pack();


        setTrayIcon();

        mainFrame.setVisible(appVisibility);
    }


    public static String convertMillisToTime(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static void timeIsUp() {
        //textarea.setText("Time is up!");
        if (!mainFrame.isVisible())
            mainFrame.setVisible(true);
        long c = System.currentTimeMillis();
        simpleTimer.stop();
        long m = System.currentTimeMillis();
    }





    public static void updateTextArea(long timerMillis) {
        long millis = timerMillis - System.currentTimeMillis();


        if (millis > 900) {
            if (lastS != (millis / 1000)) {
                String hms = convertMillisToTime(millis);
                log.info("with " + millis + "remaining change timer to " + hms);
                textarea.setText(hms);
                lastS = millis / 1000;
            }
        }


        else if (!SimpleTimer.timerStopped) {
            log.info("time is up");
            textarea.setText("Time is up");
            log.info("stop timer");
            SimpleTimer.timerStopped = true;
            // stop all and notify that timer gone
            timeIsUp();
            // sending request to spring
            log.info("send request");
            RequestSender.sendRequest(iterationTimeMs);
        }
    }






    private static PopupMenu setupTrayMenu() {
        PopupMenu trayMenu = new PopupMenu();

        // show app
        MenuItem openItem = new MenuItem("Show settings");
        openItem.addActionListener((ActionEvent e) -> mainFrame.setVisible(true));
        trayMenu.add(openItem);

        // run iter
        MenuItem newIterItem = new MenuItem("Run iteration");
        newIterItem.addActionListener((ActionEvent e) -> {
                textarea.setText(convertMillisToTime(iterationSeconds*1000));
                new Thread(() -> {
                    simpleTimer = new SimpleTimer(iterationSeconds);
                    simpleTimer.count();
                }).start();
        });
        trayMenu.add(newIterItem);


        // close app
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener((ActionEvent e) -> System.exit(0));
        trayMenu.add(exitItem);


        return trayMenu;
    }

    /**
     * ad-hoc solution for for opening trayMenu on clicking left and right mouse buttons
     * @param trayMenu trayMenu, that opened
     * @return full configurated listener
     */
    private static MouseListener implMouseListenerAndConfigureFrame(PopupMenu trayMenu) {
        // ad-hoc solution
        final Frame frame = new Frame("");
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setType(Window.Type.UTILITY);
        frame.setVisible(true);

        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getButton() == MouseEvent.BUTTON1) || (e.getButton() == MouseEvent.BUTTON2)) {
                    frame.add(trayMenu);
                    trayMenu.show(frame, e.getXOnScreen(), e.getYOnScreen());
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }



    private static void setTrayIcon() {
        if(! SystemTray.isSupported() ) {
            return;
        }

        PopupMenu trayMenu = setupTrayMenu();

        URL imageURL = TrayIcon.class.getResource(ICON_STR);

        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        TrayIcon trayIcon = new TrayIcon(icon, APPLICATION_NAME);
        trayIcon.setPopupMenu(trayMenu);
        trayIcon.setImageAutoSize(true);


        // open menu by clicking
        trayIcon.addMouseListener(implMouseListenerAndConfigureFrame(trayMenu));



        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        trayIcon.displayMessage(APPLICATION_NAME, "Application started!",
                TrayIcon.MessageType.INFO);
    }
}
