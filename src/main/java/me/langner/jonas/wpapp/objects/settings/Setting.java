package me.langner.jonas.wpapp.objects.settings;

import java.io.*;

public class Setting {

    public enum Type {
        LAST_FILE_CHOOSER_LOCATION("lfcl.set");

        private final String[] fileNames;

        Type(String ... fileNames) {
            this.fileNames = fileNames;
        }
    }

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
                if (!file.mkdirs()) {
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

    public void set(String value) throws IOException {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("File was moved or modified.");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
        writer.write(value);
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
}
