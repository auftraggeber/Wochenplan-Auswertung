package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;

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

    private static StaffEntryFilter active = null;

    public static StaffEntryFilter getActive() {
        if (active == null) {
            reset();
        }

        return active;
    }

    public static void reset() {
        active = new StaffEntryFilter(null) {
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
        WPAPP.getWochenplan().clearFilterPeriod();
        WPAPP.getWochenplan().resetFilteredMachines();
    }

    private StaffEntryFilter decorated;
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
        String dec = decorated == null ? "null" : decorated.getClass().getSimpleName();
        System.out.println("Registered new filter: " + this.getClass().getSimpleName() + " decorating " + dec);
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
     * Diese Methode wird von den {@link javax.swing.ListCellRenderer<StaffEntryFilter>} genutzt.
     * @return Gibt die kurze Beschreibung des Filters aus.
     */
    @Override
    public String toString() {
        return getShortDescription();
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
}
