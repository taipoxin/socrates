package by.tiranid.timer;


import by.tiranid.swing.MainGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SimpleTimer {


    public static boolean timerStopped = true;
    private final MainGUI mainGuiClass;
    // for counting
    private final long timeMillis;
    // minimal time = 1 second
    private final long timerSeconds;
    private final int delay;
    private Timer timer;


    public SimpleTimer() {
        this(10);
    }

    public SimpleTimer(int seconds) {
        this(seconds, (MainGUI) null);
    }


    public SimpleTimer(int seconds, int delay) {
        this(seconds, delay, null);
    }

    public SimpleTimer(int seconds, MainGUI mainGuiClass) {
        this(seconds, 50, mainGuiClass);
    }


    public SimpleTimer(int seconds, int delay, MainGUI mainGuiClass) {
        this.mainGuiClass = mainGuiClass;
        this.delay = delay;
        timerSeconds = seconds;
        timeMillis = timerSeconds * 1000;
    }


    public SimpleTimer(int count, Dimensions dimension, MainGUI mainGuiClass) {
        delay = 50;
        this.mainGuiClass = mainGuiClass;
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
            default:
                timerSeconds = 0;
                break;
        }
        timeMillis = timerSeconds * 1000;
    }


    private Timer createTimer(int delayMs, long endMs) {
        return new Timer(delayMs, (ActionEvent e) -> {
            if (timerStopped)
                return;
            mainGuiClass.updateTimerTextArea(endMs);
        });
    }


    //
    public void startNewTimer() {
        if (!timerStopped) {
            timer.stop();
            timerStopped = true;
        }
        long now = System.currentTimeMillis();
        mainGuiClass.iterationTimeMs = now;
        long endMs = now + timeMillis;
        mainGuiClass.updateTimerTextArea(endMs);
        timer = createTimer(delay, endMs);

        timerStopped = false;
        timer.start();


    }

    public void stop() {
        if (timer == null)
            return;
        timer.stop();
        // notify timer is gone
        timerStopped = true;
    }








}
