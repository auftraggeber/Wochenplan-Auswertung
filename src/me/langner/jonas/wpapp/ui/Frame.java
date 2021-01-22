package me.langner.jonas.wpapp.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Einfache Fenster mit Standardzeugs.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class Frame extends JFrame {

    public static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel panel;

    public Frame(String name, int width, int height) {
        super(name);

        this.panel = new JPanel();

        if (width > SCREEN_DIMENSION.width)
            width = SCREEN_DIMENSION.width;

        if (height > SCREEN_DIMENSION.height)
            height = SCREEN_DIMENSION.height;

        super.setLayout(null);
        super.setSize(width, height);
        super.setResizable(false);

        super.setLocation((SCREEN_DIMENSION.width - width) / 2, (SCREEN_DIMENSION.height - height) / 2);

        panel.setBounds(0,0,width,height);
        panel.setLayout(null);
        panel.setVisible(true);

        super.setVisible(false);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public JPanel getPanel() {
        return panel;
    }

    /**
     * Lädt das View neu.
     */
    public void reload() {
        super.remove(panel);
        panel.setVisible(false);
        panel.setVisible(true);
        super.add(panel);
    }

    /**
     * Zeigt die View an.
     */
    public void open() {
        super.add(panel);
        panel.setVisible(true);
        super.setVisible(true);
    }

    /**
     * Fügt ein Element zu dem Panel hinzu.
     * @param component Das hinzuzufügende Element.
     */
    public void addToPanel(Component component) {
        panel.add(component);
    }
}
