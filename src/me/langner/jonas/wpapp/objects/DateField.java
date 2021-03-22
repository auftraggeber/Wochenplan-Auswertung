package me.langner.jonas.wpapp.objects;

import me.langner.jonas.wpapp.WPAPP;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Ist eine Textkomponente, welche nur Daten annimmt.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class DateField extends JFormattedTextField {

    public static enum DateType {
        START, END;
    }

    /**
     * Ermittelt den ersten Tag des letzten Monats.
     * @param month Der Monat.
     * @param before Monate die zurückgegangen werden soll.
     * @return Das Datum des Tages.
     */
    public static Date getFirstDayOfMonth(Month month, int before) {
        int year = LocalDate.now().getYear();

        int newMonth = month.ordinal() - before;
        int newYear = year;

        /* wenn neuer Monat keinen Sinn ergibt: Jahr anpassen */
        if (newMonth < 0) {
            // Dezember des Vorjahres
            newYear--;
            newMonth = 12 + newMonth;
        }

        /* ausgeben */
        return new Date(newYear - 1900, newMonth, 1);
    }

    /**
     * Ermittelt den letzten Tag eines Monats.
     * @param month Der Monat.
     * @param before Monate die zurückgegangen werden soll.
     * @return Das Datum des Tages.
     */
    public static Date getLastDayOfMonth(Month month, int before) {
        /* datum des monats ermitteln */
        Date date = getFirstDayOfMonth(month, before);

        YearMonth yMonth = YearMonth.of(date.getYear() + 1900, date.getMonth()+1);
        int maxDate = yMonth.lengthOfMonth();

        /* Datum ausgeben */
        return new Date(date.getYear(), yMonth.getMonth().ordinal(), maxDate);
    }

    private DateType dateType;

    /**
     * Creates a new Textfeld.
     * @param dateType Typ des Datums.
     */
    public DateField(DateType dateType) {
        super(WPAPP.DISPLAY_FORMAT);
        this.dateType = dateType;

        /* ermitteln, welches Datum */
        if (dateType.equals(DateType.START))

            setText(WPAPP.DISPLAY_FORMAT.format(getFirstDayOfMonth(LocalDate.now().getMonth(),1)));
        else
            setText(WPAPP.DISPLAY_FORMAT.format(getLastDayOfMonth(LocalDate.now().getMonth(),1)));


        addListener();
    }

    /**
     * Fügt dem Feld ein Listener hinzu.
     */
    private void addListener() {
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                /* nur Zahlen */
                setText(getText().replaceAll("[^\\d.]", ""));

                /* entfernen soll keine Punkte verursachen */
                if (e.getKeyCode() != 8 && e.getKeyCode() != 127)
                    checkDot();

                /* Punkt soll automatisch gesetzt werden */
                if (e.getKeyCode() == 46) {
                    if (getText().length() != 3 && getText().length() != 6) {
                        setText(getText().substring(0,getText().length()-1));
                    }
                }
            }
        };

        addKeyListener(listener);
    }

    /**
     * Überprüft, ob als nächstes ein punkt notwendig ist.
     */
    private void checkDot() {
        int length = getText().length();

        /* Falls bestimmte länge: muss ein punkt gesetzt werden */
        if (length == 2 || length == 5 && !getText().endsWith("."))
            setText(getText() + ".");
    }

    public DateType getDateType() {
        return dateType;
    }
}