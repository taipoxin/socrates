package by.tiranid;

import by.tiranid.swing.layouts.MainGUI;
import by.tiranid.utils.MainUtils;
import by.tiranid.web.RequestSender;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Started main thread");
        MainUtils.loadDefaultProperties();
        MainGUI mpl = new MainGUI();

        SwingUtilities.invokeLater(mpl::setupUI);

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String server_Ip = addr.getHostAddress();
            RequestSender.setServerIp(server_Ip);
        } catch (UnknownHostException e) {
            log.error("exception with getting local host");
            RequestSender.setServerIp("localhost");
        }
    }

}
