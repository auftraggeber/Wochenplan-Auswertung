package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;

import java.io.Serializable;
import java.util.*;

public abstract class StaffEntryFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final HashMap<UUID, StaffEntryFilter> FILTERS = new HashMap<>();

    public static Collection<StaffEntryFilter> getStaffEntryFilters() {
        return FILTERS.values();
    }

    public static List<StaffEntry> filterStaffEntriesByAllFilters(final Collection<StaffEntry> entries) {
        List<StaffEntry> list = new ArrayList<>(entries);

        for (StaffEntryFilter filter : getStaffEntryFilters()) {
            list = filter.filterStaffEntries(list);
        }

        return list;
    }

    private final UUID id;

    protected StaffEntryFilter() {
        UUID id = null;

        do {
            id = UUID.randomUUID();
        }
        while (FILTERS.containsKey(id));

        this.id = id;
        FILTERS.put(id, this);
        System.out.println("Registered new filter: " + this.getClass().getSimpleName());

        if (this instanceof PeriodFilter)
            WPAPP.getWochenplan().clearFilterPeriod();
    }

    /**
     * Filtert einen Eintrag je nach Filtereinstellungen.
     * @param entry Der Eintrag, welcher gefiltert werden soll.
     * @return Gibt an, ob der Filter diesen Eintrag erlaubt oder, ob dieser Eintrag vom Filter verworfen werden soll.
     */
    public abstract boolean filter(StaffEntry entry);

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

    public List<StaffEntry> filterStaffEntries(final Collection<StaffEntry> entries) {
        List<StaffEntry> filtered = new ArrayList<>();

        for (StaffEntry entry : entries) {
            if (filter(entry))
                filtered.add(entry);
        }

        return Collections.unmodifiableList(filtered);
    }

    public UUID getId() {
        return id;
    }

    public void remove() {
        FILTERS.remove(this.getId());
    }
}
