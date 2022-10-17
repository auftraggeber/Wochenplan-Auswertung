package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;

import java.util.Date;

/**
 * Ein Filter der nach dem Startdatum eines {@link StaffEntry}s filtert.
 * Nur Einträge, die nach dem im Filter angegebenen Startdatum existieren,
 * passieren den Filter.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 14.10.22
 */
public class StartFilter extends StaffEntryFilter {

    private final Date start;

    public StartFilter(final Date start) {
        super();
        this.start = start;
        WPAPP.getWochenplan().clearFilterPeriod();
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        System.out.println(start + " compared to " + entry.getDate());
        return start == null || start.before(entry.getDate()) || start.equals(entry.getDate());
    }

    public Date getStart() {
        return start;
    }
}
