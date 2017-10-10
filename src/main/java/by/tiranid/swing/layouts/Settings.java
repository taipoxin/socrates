package by.tiranid.swing.layouts;

import by.tiranid.swing.component.LogJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Settings {

    private static final String FRAME_NAME = "Settings";
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 300;

    private static String iteration_time = "25 min";
    private static String pause_time = "5 min";

    private final JFrame frame;

    public Settings() {
        frame = new LogJFrame(FRAME_NAME);
    }

    void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    private JPanel setupJPanel() {
        JPanel panel = new JPanel();
        JTextField textFieldIter = new JTextField(iteration_time);
        panel.add(textFieldIter);

        textFieldIter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (!(ch >= '0' && ch <= '9'))
                    e.consume();
            }
        });
/*
        textFieldIter.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                String text = event.getText().toString();
                if (!(text.contains("min"))) {
                    text = text.trim();
                    text += " min";
                    ((JTextField)event.getSource()).setText(text);
                }
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {

            }
        });
*/
        /**
         * add " min" handling
         */
        textFieldIter.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                JTextField field = (JTextField) e.getSource();
                String text = field.getText();
                if (!(text.contains("min"))) {
                    text = text.trim();
                    text += " min";
                    field.setText(text);
                }
            }
        });

        JTextField textFieldPause = new JTextField(pause_time);
        panel.add(textFieldPause);
        JTextField textField = new JTextField("Sample text");
        panel.add(textField);


        return panel;
    }

    JFrame setupJFrame() {
        frame.setLocationRelativeTo(null);
        // hiding in tray
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel mainPane = setupJPanel();
        frame.setContentPane(mainPane);
        frame.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.pack();
        frame.setVisible(false);
        return frame;
    }
}
