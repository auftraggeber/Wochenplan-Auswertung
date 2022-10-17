package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;

import java.util.Date;

/**
 * Ein Filter, der nach dem Enddatum filtert. Funktioniert ähnlich wie {@link StartFilter}.
 * Nur Einträge, dessen Datum vor dem im Filter angegebenen Enddatum liegt, passieren den
 * Filter.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 14.10.22
 */
public class EndFilter extends StaffEntryFilter {

    private Date end;

    public EndFilter(Date end) {
        super();
        this.end = end;
        WPAPP.getWochenplan().clearFilterPeriod();
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return end == null || end.after(entry.getDate()) || end.equals(entry.getDate());
    }

    public void setEnd(Date end) {
        this.end = end;
        WPAPP.getWochenplan().clearFilterPeriod();
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String getLongDescription() {
        return "Enddatum: " + WPAPP.DISPLAY_FORMAT.format(getEnd());
    }

    @Override
    public String getShortDescription() {
        return getLongDescription();
    }
}
