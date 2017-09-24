package by.tiranid.swing.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class LogJFrame extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(LogJFrame.class);

    public LogJFrame() throws HeadlessException {
        super();
        frameInit();
    }


    public LogJFrame(String title) throws HeadlessException {
        super(title);
        frameInit();
    }


    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        log.info("set visibility {}", b);
    }
}
