package me.langner.jonas.wpapp.objects;

import me.langner.jonas.wpapp.WPAPP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Bildet eine Zeitspanne ab.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class Period {

    private Date start, end;


    /**
     * Erstellt eine neue Zeitspanne.
     * @param start Das Startdatum.
     * @param end Das Enddatum.
     */
    public Period(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    /**
     * Gibt das Startdatum in schöner Form aus.
     * @return Startdatum als (yy.MM.yyyy)
     */
    public String getStartDisplay() {
        return WPAPP.DISPLAY_FORMAT.format(start);
    }

    /**
     Gibt das Enddatum in schöner Form aus.
     * @return Enddatum als (yy.MM.yyyy)
     */
    public String getEndDisplay() {
        return WPAPP.DISPLAY_FORMAT.format(end);
    }
}
