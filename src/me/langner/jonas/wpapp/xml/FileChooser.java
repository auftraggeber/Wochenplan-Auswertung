package me.langner.jonas.wpapp.xml;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Wählt eine Datei aus.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class FileChooser {

    private JFileChooser jFileChooser;
    private File file;

    public FileChooser() {
        jFileChooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");

        addFilter();
        open();
    }

    /**
     * Fügt einen Filter nach .wpapp-Dateien hinzu.
     */
    private void addFilter() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Wochenplan Daten", "wpapp");
        jFileChooser.setFileFilter(filter);
    }

    /**
     * Öffnet die Dateiauswahl.
     */
    private void open() {

        int result = jFileChooser.showOpenDialog(null);

        /* überprüfen, ob Datei gewählt */
        if (result == JFileChooser.APPROVE_OPTION) {
            // Datei geöffnet
            File file = jFileChooser.getSelectedFile();

            /* überprüfen, ob richtiges Format */
            if (file.getAbsolutePath().endsWith(".wpapp")) {
                // richtiges Format

                this.file = file;
            }
            else {
                // TODO: sinnvoller Abbruch
            }
        }
    }

    public File getFile() {
        return file;
    }

}
