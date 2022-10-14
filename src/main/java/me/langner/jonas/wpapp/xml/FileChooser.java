package me.langner.jonas.wpapp.xml;

import me.langner.jonas.wpapp.objects.exception.IllegalFileExtensionException;
import me.langner.jonas.wpapp.objects.settings.Setting;
import me.langner.jonas.wpapp.objects.ui.frames.ErrorUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wählt eine Datei aus.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class FileChooser {

    private JFileChooser jFileChooser;
    private File[] files;

    public FileChooser() {
        System.out.println(getStartLocation());
        jFileChooser = new JFileChooser(getStartLocation());
        jFileChooser.setMultiSelectionEnabled(true);
        addFilter();

        while (this.files == null) {
            try {
                this.files = open();
            } catch (FileNotFoundException e) {
                break;
            } catch (IllegalFileExtensionException e) {
                new ErrorUI("Sie haben eine falsche Datei gewählt", e);
            }
        }

    }

    /**
     * Ermittelt, welchen Pfad der Chooser anzeigen soll.
     * @return Der Pfad, der standardmäßig geöffnet ist.
     */
    private String getStartLocation() {
        final Setting setting = Setting.getSetting(Setting.Type.LAST_FILE_CHOOSER_LOCATION);

        if (setting != null) {
            try {
                String location = setting.getString();

                if (location != null)
                    return location;

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return System.getProperty("user.home") + "/Desktop";
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
    private File[] open() throws FileNotFoundException, IllegalFileExtensionException {

        int result = jFileChooser.showOpenDialog(null);

        /* überprüfen, ob Datei gewählt */
        if (result == JFileChooser.APPROVE_OPTION) {
            // Datei geöffnet
            File[] files = jFileChooser.getSelectedFiles();

            List<File> rightFiles = new ArrayList<>();

            for (File file : files) {
                if (file.getAbsolutePath().endsWith(".wpapp")) {
                    rightFiles.add(file);
                }
            }

            /* überprüfen, ob richtiges Format */
            if (rightFiles != null && !rightFiles.isEmpty()) {
                // richtiges Format
                File[] fileArray = new File[rightFiles.size()];

                for (int i = 0; i < fileArray.length; i++) {
                    fileArray[i] = rightFiles.get(i);
                }

                final String location = fileArray[0].getParent();
                final Setting locationSetting = Setting.getSetting(Setting.Type.LAST_FILE_CHOOSER_LOCATION);

                if (locationSetting != null) {
                    try {
                        locationSetting.set(location);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }

                return fileArray;
            }
            else {
                if (rightFiles == null || files == null || files.length <= 0)
                    throw new FileNotFoundException("The filechooser returned no file.");
                else
                    throw new IllegalFileExtensionException("The chosen file has not the extension .wpapp", "wpapp");

            }
        }
        else throw new FileNotFoundException("The chooser was canceled.");
    }

    public File[] getFiles() {
        return files;
    }

}
