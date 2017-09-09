package by.tiranid.timer;


import by.tiranid.swing.TrayIconImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SimpleTimer {

    // for counting
    long timeMillis;
    // minimal time = 1 second
    long timerSeconds;



    public SimpleTimer() {
        // test default value
        timerSeconds = 10;
        timeMillis = timerSeconds * 1000;

    }



    public SimpleTimer(int seconds) {
        timerSeconds = seconds;
        timeMillis = timerSeconds * 1000;
    }

    public SimpleTimer(int count, Dimensions dimension) {
        switch (dimension) {
            case SECOND:
                timerSeconds = count;
                break;
            case MINUTE:
                timerSeconds = count * 60;
                break;
            case HOUR:
                timerSeconds = count * 3600;
                break;
        }
        timeMillis = timerSeconds * 1000;
    }

    Timer timer;

    public void count() {
        long now = System.currentTimeMillis();
        TrayIconImpl.iterationTimeMs = now;
        long endMs = now + timeMillis;
        notifyTimer(endMs);
        timer = new Timer(500, (ActionEvent e) -> {
            notifyTimer(endMs);
        });
        timer.start();
    }

    public void stop() {
        timer.stop();
        // notify timer is gone
    }

    public void notifyTimer(long ms) {
        TrayIconImpl.updateTextArea(ms);
    }






}
