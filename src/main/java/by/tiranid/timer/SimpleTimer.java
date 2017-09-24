package by.tiranid.timer;


import by.tiranid.swing.TrayIconImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SimpleTimer {

    // for counting
    long timeMillis;
    // minimal time = 1 second
    long timerSeconds;

    public static boolean timerStopped = true;


    private TrayIconImpl mainGuiClass;


    public SimpleTimer() {
        // test default value
        timerSeconds = 10;
        timeMillis = timerSeconds * 1000;

    }


    public SimpleTimer(int seconds, TrayIconImpl mainGuiClass) {
        this.mainGuiClass = mainGuiClass;
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
        if (!timerStopped) {
            timer.stop();
            timerStopped = true;
        }
        long now = System.currentTimeMillis();
        mainGuiClass.iterationTimeMs = now;
        long endMs = now + timeMillis;
        notifyTimer(endMs);
        timer = new Timer(10, (ActionEvent e) -> {
            if (timerStopped)
                return;
            notifyTimer(endMs);
        });
        timer.start();
        timerStopped = false;

    }

    public void stop() {
        timer.stop();
        // notify timer is gone
        SimpleTimer.timerStopped = false;
    }

    public void notifyTimer(long ms) {
        mainGuiClass.updateTextArea(ms);
    }







}
