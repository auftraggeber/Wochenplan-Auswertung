package me.langner.jonas.wpapp.objects.factory;

import me.langner.jonas.wpapp.objects.StaffEntry;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Eine Klasse, die die Implementation zum Vergleich von {@link StaffEntry}s enthält.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 18.10.22
 */
public class StaffEntryComparator implements Comparator<StaffEntry>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(StaffEntry entry1, StaffEntry entry2) {
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
    }
}
