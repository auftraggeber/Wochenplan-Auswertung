package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.objects.StaffEntry;

/**
 * Stellt die Funktion, mit der ein Filter überprüft, ob ein Eintrag passiert, zur Verfügung.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 14.10.22
 */
public interface IStaffEntryFilter {

    /**
     * Überprüft, ob ein Filter den Eintrag passieren lässt.
     * @param entry Der Eintrag, welcher vom
     * @return
     */
    boolean staffEntryGetsAccepted(StaffEntry entry);

}
