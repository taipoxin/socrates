package by.tiranid.swing.listeners;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Slf4j
public class LeftClickMouseListener implements MouseListener {

    private Frame frame;
    private PopupMenu menu;

    public LeftClickMouseListener(Frame frame, PopupMenu menu) {
        this.frame = frame;
        this.menu = menu;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // left and right button
        if ((e.getButton() == MouseEvent.BUTTON1) || (e.getButton() == MouseEvent.BUTTON2)) {
            frame.add(menu);
            menu.show(frame, e.getXOnScreen(), e.getYOnScreen());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
