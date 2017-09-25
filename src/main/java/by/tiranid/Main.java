package by.tiranid;

import by.tiranid.swing.MainGUI;
import by.tiranid.utils.MainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Started main thread");
        MainUtils.loadDefaultProperties();
        MainGUI mpl = new MainGUI();

        SwingUtilities.invokeLater(mpl::setupUI);
    }
}
