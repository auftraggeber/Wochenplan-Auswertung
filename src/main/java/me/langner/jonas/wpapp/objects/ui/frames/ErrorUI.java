package me.langner.jonas.wpapp.objects.ui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Verwaltet die Darstellung von Fehlern.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class ErrorUI extends Frame {

    private JLabel messageLabel;
    private JLabel link = new JLabel(
            "<html><a href=\"mailto:jonas.langner@weidplas.com\">Melden Sie den Fehler per Mail.</a></html>",
            JLabel.CENTER
    );

    /**
     * Erstellt ein neues Fenster mit einer Fehlermeldung.
     * @param message Die generelle Fehlermeldung.
     * @param exception Der interne Fehler.
     * @param addition Zusätzliches.
     */
    public ErrorUI(String message, Exception exception, String ... addition) {
        super("Fehler: " + exception.getClass().getName(), 500, 200);

        getPanel().setBackground(Color.WHITE);

        /* größe wirklich anpassen */
        setMinHeight(270);
        setSize(500,270);

        /* Standardverhalten definieren */
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setState(JFrame.ERROR);

        message += "<br/><br/><strong>Das Programm meldet:</strong><br/><i>\"" + exception.getMessage() + "";

        for (String add : addition) {
            message += "<br/>" + add;
        }

        messageLabel = new JLabel(
                "<html>" +
                        "<p>" +
                        "<strong>" +
                        "Es ist ein Fehler aufgetreten:" +
                        "</strong>" +
                        "</p><br/>" +
                        message + "\"</i>" +
                        "</html>"
        );
        messageLabel.setBounds(40,10,getWidth() - 80,getHeight() - 20);
        messageLabel.setForeground(Color.RED);
        messageLabel.setVerticalAlignment(JLabel.TOP);
        messageLabel.setFont(messageLabel.getFont().deriveFont(13f));

        /* Mail-Link hinzufügen */
        link.setBounds(100,210, getWidth() - 200, 40);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /* beim klicken link öffnen */
        link.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // hat auf Link geklickt
                try {
                    Desktop.getDesktop().browse(new URI(
                            ("mailto:jonas.langner@weidplas.com" +
                                    "?subject=Auswertung+Fehler&body=" + URLEncoder.encode(
                                            getTitle() + "\n\n\nJava-Fehlermeldung: \"" + exception.getMessage() + "\"",
                                    StandardCharsets.ISO_8859_1.toString()
                            )).replaceAll("\\+","%20")
                    ));
                }
                catch (URISyntaxException | IOException ex) {
                    link.setText("Der Link konnte nicht geöffnet werden. Mail-Adresse: jonas.langner@weidplas.com");
                }
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
        });

        /* frame aufbauen */
        addToPanel(this.messageLabel,link);
        setVisible(true);
        reload();
    }

    @Override
    public void onResize(ComponentEvent event) {

    }
}
