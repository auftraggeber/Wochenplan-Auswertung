package me.langner.jonas.wpapp.objects.ui.frames.filter.listener;

import me.langner.jonas.wpapp.objects.ui.frames.filter.FilterUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class OpenFilterUIOnWindowCloseListenerImpl implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        new FilterUI();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
