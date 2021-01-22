package me.langner.jonas.wpapp.objects;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Erbe für alle Geräte aus Werk.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public abstract class FactoryElement {

    private TreeSet<StaffEntry> entries = new TreeSet<StaffEntry>((entry1, entry2) -> {
        if (entry1.getDate().before(entry2.getDate()))
            return -1;
        else if (entry1.getDate().after(entry2.getDate()))
            return 1;

        if (entry1.getShift() < entry2.getShift())
            return -1;
        else if (entry1.getShift() > entry2.getShift())
            return 1;

        if (entry1.getMachine().getId() < entry2.getMachine().getId())
            return -1;
        else if (entry1.getMachine().getId() > entry2.getMachine().getId())
            return 1;

        if (entry1.getTool().getId() < entry2.getTool().getId())
            return -1;
        else if (entry1.getTool().getId() > entry2.getTool().getId())
            return 1;

        return 0;
    });
    private int id;
    private String name;

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
        if (!entries.contains(entry))
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

    public Set<StaffEntry> getEntries() {
        return Collections.unmodifiableSet(entries);
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
}
