package by.tiranid;

import by.tiranid.swing.MainGUI;
import by.tiranid.utils.MainUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Started main thread");
        MainUtils.loadDefaultProperties();
        MainGUI mpl = new MainGUI();

        SwingUtilities.invokeLater(mpl::setupUI);
    }

}
