package by.tiranid.swing.component;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class LogJTextArea extends JTextArea {



    public LogJTextArea(String text) {
        super(text);
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        log.info("set text to {}", t);
    }
}
