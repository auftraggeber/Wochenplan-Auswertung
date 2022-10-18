package me.langner.jonas.wpapp.objects.settings;

import me.langner.jonas.wpapp.objects.filter.StaffEntryFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Eine Klasse, die bestimmte Einstellungen persistiert.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 14.10.22
 */
public class Setting {

    private static final String FILTER_DIR = "flt";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private static File settingsDir = null;

    /**
     * Erstellt die Instanz anhand eines Einstellungstyps.
     * @param type Der Einstellungstyp.
     * @return Die erstellte Instanz oder null, wenn ein Fehler aufgetreten ist.
     */
    public static Setting getSetting(Type type) {
        try {
            return new Setting(type.fileNames);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ermittelt den Speicherort für einen Filter
     * @param filterName Der Name des Filters.
     * @return Der Ort, an dem der Filter gespeichert werden kann.
     */
    public static Setting getFilterLocation(final String filterName) {
        try {
            return new Setting(FILTER_DIR, filterName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gibt den Speicherort für einen Filter aus.
     * @param filter Der Filter, der abgespeichert werden soll.
     * @return Der Speicherort des Filters.
     */
    public static Setting getFilterLocation(final StaffEntryFilter filter) {
        return getFilterLocation(filter.getName());
    }

    /**
     * Ermittelt die Namen aller persistierten Filter.
     * @return Eine Liste mit allen Filternamen.
     */
    public static List<String> getPersistedFilterNames() {
        List<String> names = new ArrayList<>();

        File root = getSettingsDir();

        if (root != null && root.exists() && root.isDirectory()) {
            File filterDir = new File(root.getAbsolutePath() + FILE_SEPARATOR + FILTER_DIR);

            if (filterDir.exists() && filterDir.isDirectory()) {
                try{
                    for (File file : Objects.requireNonNull(filterDir.listFiles())) {
                        if (file.exists() && !file.isDirectory()) {
                            names.add(file.getName());
                        }
                    }
                }
                catch (NullPointerException ignored) {}
            }
        }

        return names;
    }

    /**
     * Ermittelt den generellen Ordner, in dem die Sicherungsdateien abgelegt werden können.
     * @return Der Ordner, in dem die Dateien abgelegt werden können.
     */
    private static File getSettingsDir() {
        if (settingsDir != null && settingsDir.exists() && settingsDir.isDirectory())
            return settingsDir;

        String home = System.getProperty("user.home");

        File settingsDir = new File(home + FILE_SEPARATOR + "Wochenplan Auswertung");

        if (!settingsDir.exists() || !settingsDir.isDirectory()) {
            if (!settingsDir.mkdirs()) {
                return null;
            }
        }

        return settingsDir;
    }

    /**
     * Hält die verschiedenen Einstellungstypen sowie die Pfade der Sicherungsdateien.
     */
    public enum Type {
        LAST_FILE_CHOOSER_LOCATION("lfcl.set");

        private final String[] fileNames;

        Type(String ... fileNames) {
            this.fileNames = fileNames;
        }
    }

    private File file;

    /**
     * Erstellt eine neue Instanz und baut die benötigte Dateistruktur auf.
     * @param fileNames Der Pfad der Datei, welcher angelegt werden soll.
     *                  Der letzte Name gibt den Dateinamen an. Die Namen davor geben den Pfad an.
     * @throws IOException Fehler beim Erstellen der Datei.
     * @throws IllegalArgumentException Bei keiner Dateiangabe.
     * @throws IllegalStateException Diverse Fehler beim Erstellen des Pfades.
     */
    private Setting(String ... fileNames) throws IOException, IllegalArgumentException, IllegalStateException {
        if (fileNames == null || fileNames.length <= 0) {
            throw new IllegalArgumentException("No fileName given.");
        }

        if (getSettingsDir() == null) {
            throw new IllegalStateException("Dir for setting not found.");
        }

        String path = getSettingsDir().getAbsolutePath();

        for (int i = 0; i < fileNames.length; i++) {
            final boolean mkDir = i < fileNames.length - 1;

            path += FILE_SEPARATOR + fileNames[i];
            final File file = new File(path);
            if (mkDir) {
                if (!file.exists() && !file.isDirectory() && !file.mkdirs()) {
                    throw new IllegalStateException("Could not create dir: " + path);
                }
            }
            else {
                if (file.exists() || file.createNewFile()) {
                    this.file = file;
                }
                else throw new IllegalStateException("Could not create or find file: " + path);
            }

        }
    }

    /**
     * Speichert einen Wert in der Sicherungsdatei.
     * @param value Der Wert der abgesichert werden soll.
     * @throws IOException Fehler, die beim Abspeichern in der Datei entstanden sind.
     */
    public void set(String value) throws IOException {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("File was moved or modified.");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
        writer.write(value);
        writer.flush();
        writer.close();
    }

    /**
     * Speichert einen Wert in der Sicherungsdatei.
     * @param serializable Das Objekt, welches abgesichert werden soll.
     * @throws IOException Fehler, die beim Abspeichern in der Datei entstanden sind.
     */
    public void set(final Serializable serializable) throws IOException {
        ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(this.file));

        writer.writeObject(serializable);
        writer.flush();
        writer.close();
    }

    public String getString() throws FileNotFoundException {

        if (this.file != null && this.file.exists()) {
            BufferedReader reader = new BufferedReader(
                    new FileReader(this.file)
            );

            try {
                StringBuilder value = new StringBuilder();

                while(true) {
                    String line = reader.readLine();

                    if (line == null) {
                        reader.close();
                        return value.toString();
                    }

                    if (!value.toString().equals(""))
                        value.append("\n");

                    value.append(line);
                }
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }

            return null;
        }

        throw new FileNotFoundException("File was moved or modified.");
    }

    public Object getObject() throws FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException("File was moved or modified.");
        }
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(this.file));
            Object object = stream.readObject();
            stream.close();

            return object;
        }
        catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }


        return null;
    }
}
