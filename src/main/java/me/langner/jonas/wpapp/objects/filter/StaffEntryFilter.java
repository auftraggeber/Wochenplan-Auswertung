package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.settings.Setting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Eine Klasse, die die typischen Filterfunktionen zum Filtern von
 * {@link StaffEntry}s bereitstellt.
 * Die Filterfunktion {@link #filter(StaffEntry)} an sich wird von den Unterklassen implementiert.
 * Nutzt dabei des Dekorierermuster.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 14.10.22
 */
public abstract class StaffEntryFilter implements Serializable, IStaffEntryFilter {

    private static final long serialVersionUID = 1L;

    private static StaffEntryFilter active = null;

    /**
     * @return Der aktuell aktive Filter.
     */
    public static StaffEntryFilter getActive() {
        if (active == null) {
            reset();
        }

        return active;
    }

    /**
     * Setzt den Filter zurück.
     * Jeder Datensatz wird wieder angezeigt.
     */
    public static void reset() {
        active = new StaffEntryFilter(null) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean staffEntryGetsAccepted(StaffEntry entry) {
                return true;
            }

            @Override
            public String getLongDescription() {
                return "Basis: Alle Einträge";
            }

            @Override
            public String getShortDescription() {
                return getLongDescription();
            }
        };
        WPAPP.getWochenplan().resetAllFilters();
    }

    /**
     * Aktualisiert den aktuellen Filter.
     * Dabei wird ein Filter aus den Filterdateien geladen.
     * @param fileName Der Name des Filters und somit der Datei, in der der Filter abgespeichert wurde.
     * @return Gibt an, ob der Filter geladen wurde.
     */
    public static boolean loadFilterFromFile(final String fileName) {
        Setting setting = Setting.getFilterLocation(fileName);

        if (setting != null) {
            try {
                Object o = setting.getObject();

                if (o instanceof StaffEntryFilter) {
                    reset();
                    active = (StaffEntryFilter) o;
                    WPAPP.getWochenplan().resetAllFilters();
                }
            }
            catch (FileNotFoundException ignored) {}
        }

        return false;
    }

    private StaffEntryFilter decorated, parent;
    private String name;

    /**
     * Registriert einen neuen Filter.
     * Dabei wird der schon bestehende Filter dekoriert.
     */
    protected StaffEntryFilter() {
        this(getActive());
    }

    /**
     * Registriert einen neuen Filter.
     * @param decorated Der Filter der dekoriert werden soll.
     */
    protected StaffEntryFilter(StaffEntryFilter decorated) {
        this.decorated = decorated;
        this.name = decorated != null ? decorated.name : UUID.randomUUID().toString();
        if (decorated != null)
            decorated.setParent(this);

        active = this;
    }

    /**
     * Filtert einen Eintrag je nach Filtereinstellungen.
     * @param entry Der Eintrag, welcher gefiltert werden soll.
     * @return Gibt an, ob der Filter diesen Eintrag erlaubt oder, ob dieser Eintrag vom Filter verworfen werden soll.
     */
    public boolean filter(StaffEntry entry) {
        if (entry == null)
            return false;

        return (decorated == null || decorated.filter(entry)) && this.staffEntryGetsAccepted(entry);
    }

    /**
     * Filtert mehrere StaffEntries.
     * @param entries Die Einträge, die gefiltert werden sollen.
     * @deprecated Verwendet die Methode {@link #filterStaffEntries(Collection)} und wandelt das Array in eine {@link List<StaffEntry>} um.
     * @return Die Einträge, die den Filter passiert haben.
     */
    @Deprecated
    public StaffEntry[] filterStaffEntries(final StaffEntry[] entries) {
        List<StaffEntry> list = Arrays.asList(entries);

        list = filterStaffEntries(list);

        if (list.isEmpty()) {
            return new StaffEntry[0];
        }

        StaffEntry[] array = new StaffEntry[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);

        return array;
    }

    /**
     * Filtert mehrere StaffEntries.
     * @param entries Die Einträge, die gefiltert werden sollen.
     * @return Die Einträge, die den Filter passiert haben.
     */
    public List<StaffEntry> filterStaffEntries(final Collection<StaffEntry> entries) {
        List<StaffEntry> filtered = new ArrayList<>();

        for (StaffEntry entry : entries) {
            if (filter(entry))
                filtered.add(entry);
        }

        return Collections.unmodifiableList(filtered);
    }

    /**
     * Sichert den Filter in einer Datei.
     * Verwendet dafür {@link Setting#getFilterLocation(String)}, um die Datei zu bestimmen.
     * @return Gibt an, ob das Speichern problemlos beendet werden konnte.
     */
    public boolean persist() {
        Setting setting = Setting.getFilterLocation(this);
        try {
            setting.set(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeFromCurrentStack() {
        if (getDecorated() != null) {
            getDecorated().setParent(getParent());
            if (getParent() != null)
                getParent().setDecorated(getDecorated());
            else {
                active = getDecorated();
            }
            setParent(null);
            setDecorated(null);
        }
    }

    private void setParent(final StaffEntryFilter parent) {
        this.parent = parent;
    }

    private void setDecorated(final StaffEntryFilter decorated) {
        this.decorated = decorated;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Ermittelt den erstbesten Filter aus einem Filterstack.
     * @param clazz Die Klasse des gesuchten Filters.
     * @param <T> Der Typ des Filters.
     * @return Gibt das Filterobjekt, welches im aktuellen Filterstack liegt, an oder null, wenn kein Filter gefunden werden konnte.
     */
    public <T extends StaffEntryFilter> T getFirstFilterOfType(Class<T> clazz) {
        if (this.getClass().equals(clazz))
            return (T) this;

        if (decorated != null)
            return decorated.getFirstFilterOfType(clazz);

        return null;
    }

    public <T extends StaffEntryFilter> T getNextFilterOfType(Class<T> clazz) {
        if (decorated != null) {
            return decorated.getFirstFilterOfType(clazz);
        }

        return null;
    }

    public Vector<StaffEntryFilter> getFilterStack() {
        Vector<StaffEntryFilter> filters = getDecorated() != null ? getDecorated().getFilterStack() : new Vector<>();

        filters.add(0, this);
        return filters;
    }

    public int getFilterStackSize() {
        return getFilterStack().size();
    }

    public String getName() {
        return name;
    }

    protected StaffEntryFilter getDecorated() {
        return decorated;
    }

    protected StaffEntryFilter getParent() {
        return parent;
    }

    /**
     * Diese Methode wird von den {@link javax.swing.ListCellRenderer<StaffEntryFilter>} genutzt.
     * @return Gibt die kurze Beschreibung des Filters aus.
     */
    @Override
    public String toString() {
        return getShortDescription();
    }
}
