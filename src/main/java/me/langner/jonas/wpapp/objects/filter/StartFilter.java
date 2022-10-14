package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;

import java.util.Date;

public class StartFilter extends StaffEntryFilter {

    private final Date start;

    public StartFilter(final Date start) {
        super();
        this.start = start;
        WPAPP.getWochenplan().clearFilterPeriod();
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return start == null || start.before(entry.getDate()) || start.equals(entry.getDate());
    }

    public Date getStart() {
        return start;
    }
}
