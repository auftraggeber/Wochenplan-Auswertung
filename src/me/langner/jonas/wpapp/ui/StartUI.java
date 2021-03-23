package me.langner.jonas.wpapp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class StartUI extends Frame {

    private JLabel label;
    private static final JLabel VERSION_LABEL = new JLabel("Version 1.0.1   Build 15");

    public StartUI() throws FileNotFoundException {
        super("", 500,300);

        getPanel().setBackground(new Color(50,50,50));
        setBackground(new Color(50,50,50));

        URL imagePath = getClass().getResource("/img/logo.png");

        /* überprüfen, ob Bild existiert */
        if (imagePath != null) {
            ImageIcon icon = new ImageIcon(imagePath);
            icon = new ImageIcon(icon.getImage().getScaledInstance(500,170, Image.SCALE_FAST));

            label = new JLabel(icon);
            label.setBounds(0,0,500,300);

            VERSION_LABEL.setForeground(Color.WHITE);
            VERSION_LABEL.setFont(VERSION_LABEL.getFont().deriveFont(10f));
            VERSION_LABEL.setBounds(10,280,500,20);

            addToPanel(label,VERSION_LABEL);
            setUndecorated(true);
            setResizable(false);

            if (label.getIcon() != null) {
                open();
            }
        }
        else throw new FileNotFoundException();


    }


    @Override
    public void onResize(ComponentEvent event) {

    }
}
