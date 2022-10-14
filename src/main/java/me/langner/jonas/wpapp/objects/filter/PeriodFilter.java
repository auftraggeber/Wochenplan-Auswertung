package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.objects.StaffEntry;

import java.util.Date;

public class PeriodFilter extends StaffEntryFilter {

    private static final long serialVersionUID = 1L;

    private final Date start, end;

    public PeriodFilter(Date start, Date end) {
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean filter(StaffEntry entry) {
        Date date = entry.getDate();
        final boolean afterStart = start == null || start.before(date) || start.equals(date);

        if (!afterStart) {
            return false;
        }

        final boolean beforeEnd = end == null || end.after(date) || end.equals(date);
        return beforeEnd; // afterStart wird oben schon abgefangen (unnötige Bedingung abfangen)
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
