package me.langner.jonas.wpapp.objects.ui.frames;

import me.langner.jonas.wpapp.WPAPP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Verwaltet den Startbildschirm des Programms.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class StartUI extends Frame {

    private JLabel label;
    private static final JLabel VERSION_LABEL = new JLabel("Version " + WPAPP.VERSION + " Build " + WPAPP.BUILD + " - " + WPAPP.SINCE);

    /**
     * Erstellt einen neuen Startbildschirm.
     * @throws FileNotFoundException Wird geworfen, wenn das Logo nicht verfügbar ist.
     */
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
