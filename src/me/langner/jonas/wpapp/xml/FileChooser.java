package me.langner.jonas.wpapp.xml;

import me.langner.jonas.wpapp.objects.IllegalFileExtensionException;
import me.langner.jonas.wpapp.ui.ErrorUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.IllegalFormatException;

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

        while (this.file == null) {
            try {
                this.file = open();
            } catch (FileNotFoundException e) {
                break;
            } catch (IllegalFileExtensionException e) {
                new ErrorUI("Sie haben eine falsche Datei gewählt", e);
            }
        }

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
    private File open() throws FileNotFoundException, IllegalFileExtensionException {

        int result = jFileChooser.showOpenDialog(null);

        /* überprüfen, ob Datei gewählt */
        if (result == JFileChooser.APPROVE_OPTION) {
            // Datei geöffnet
            File file = jFileChooser.getSelectedFile();

            /* überprüfen, ob richtiges Format */
            if (file != null && file.getAbsolutePath().endsWith(".wpapp")) {
                // richtiges Format

                return file;
            }
            else {
                if (file == null)
                    throw new FileNotFoundException("The filechooser returned no file.");
                else
                    throw new IllegalFileExtensionException("The chosen file has not the extension .wpapp", "wpapp");

            }
        }
        else throw new FileNotFoundException("The chooser was canceled.");
    }

    public File getFile() {
        return file;
    }

}
