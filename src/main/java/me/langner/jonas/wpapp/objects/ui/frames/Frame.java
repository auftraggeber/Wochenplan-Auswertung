package me.langner.jonas.wpapp.objects.ui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Einfache Fenster mit Standardzeugs.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public abstract class Frame extends JFrame {

    public static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel panel;
    private int minHeight = 300, minWidth = 300;

    public Frame(String name, int width, int height) {
        super(name);

        this.panel = new JPanel();

        if (width > SCREEN_DIMENSION.width)
            width = SCREEN_DIMENSION.width;

        if (height > SCREEN_DIMENSION.height)
            height = SCREEN_DIMENSION.height - 100;

        super.setLayout(null);
        super.setSize(width, height);
        super.setResizable(true);

        super.setLocation((SCREEN_DIMENSION.width - width) / 2, (SCREEN_DIMENSION.height - height) / 2);

        panel.setBounds(0,0,width,height);
        panel.setLayout(null);
        panel.setVisible(true);

        super.setVisible(false);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* Event überprüfen */
        super.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                if (getHeight() < minHeight)
                    setSize(getWidth(), minHeight);
                if (getWidth() < minWidth)
                    setSize(minWidth, getHeight());

                getPanel().setSize(getSize());
                onResize(e);
            }
        });
    }

    /**
     * Lädt das View neu.
     */
    public void reload() {
        super.remove(panel);
        panel.setVisible(false);
        panel.setVisible(true);
        super.add(panel);
        revalidate();
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

    /**
     * Fügt Komponenten zur Sicht hinzu.
     * @param components Array mit Komponenten.
     */
    public void addToPanel(Component ... components) {
        for (Component component : components)
            addToPanel(component);
    }

    /**
     * Methode wird aufgerufen, wenn sich das Fenster verändert.
     * @param event Das Event mit allen Informationen.
     */
    public abstract void onResize(ComponentEvent event);

    public JPanel getPanel() {
        return panel;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }
}
