package by.tiranid.swing.component;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class LogJFrame extends JFrame {

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
