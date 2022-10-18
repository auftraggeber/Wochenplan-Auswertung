package me.langner.jonas.wpapp.objects.factory;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.filter.StaffEntryFilter;

import java.io.Serializable;
import java.util.*;

/**
 * Erbe für alle Geräte aus Werk.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public abstract class FactoryElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private TreeSet<StaffEntry> entries = new TreeSet<StaffEntry>(new StaffEntryComparator());
    private int id;
    private String name;

    protected FactoryElement() {
        this(-1, null);
    }

    /**
     * Erstellt ein neues Element.
     * @param id Die ID des Elements.
     */
    public FactoryElement(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Verbindet Element mit Eintrag.
     * @param entry Der Eintrag.
     */
    public void addEntry(StaffEntry entry) {
        if (entries.contains(entry)) {
            StaffEntry oldEntry = entries.floor(entry);
            System.out.println("Found duplicate: " + oldEntry);
            System.out.println("New entry: " + entry);

            try {
                entry.merge(oldEntry);
                entries.remove(oldEntry);
            }
            catch (Exception e) {
                System.err.println("Could not merge ID{" + entry.getJavaID() + "} with ID{" + oldEntry.getJavaID() + "}. Continuing...");
                return;
            }
        }

        entries.add(entry);
    }

    /**
     * Löst Element mit Eintrag.
     * @param entry
     */
    public void removeEntry(StaffEntry entry) {
        while (entries.contains(entry))
            entries.remove(entry);
    }

    public List<StaffEntry> getEntries() {
        /* filter ausgeben */
        return Collections.unmodifiableList(StaffEntryFilter.getActive().filterStaffEntries(entries));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactoryElement element = (FactoryElement) o;
        return id == element.id && Objects.equals(name, element.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
