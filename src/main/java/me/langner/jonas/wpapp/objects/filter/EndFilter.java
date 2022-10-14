package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;

import java.util.Date;

public class EndFilter extends StaffEntryFilter {

    private final Date end;

    public EndFilter(Date end) {
        super();
        this.end = end;
        WPAPP.getWochenplan().clearFilterPeriod();
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return end == null || end.after(entry.getDate()) || end.equals(entry.getDate());
    }

    public Date getEnd() {
        return end;
    }
}
