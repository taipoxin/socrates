package by.tiranid.swing.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class LogJTextArea extends JTextArea {

    private static final Logger log = LoggerFactory.getLogger(LogJTextArea.class);


    public LogJTextArea(String text) {
        super(text);
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        log.info("set text to {}", t);
    }
}
