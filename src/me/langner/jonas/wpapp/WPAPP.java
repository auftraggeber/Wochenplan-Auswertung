package me.langner.jonas.wpapp;

import me.langner.jonas.wpapp.objects.*;
import me.langner.jonas.wpapp.ui.ErrorUI;
import me.langner.jonas.wpapp.ui.StartUI;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Die Hauptklasse: von hier startet das Program.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class WPAPP {


    public static final SimpleDateFormat
            SQL_FORMAT = new SimpleDateFormat("yyyy-MM-dd"),
            DISPLAY_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final int SHIFT_EARLY = 0, SHIFT_LATE = 1, SHIFT_NIGHT = 2;

    private static Wochenplan wochenplan = new Wochenplan();
    private static StartUI ui;

    /**
     * Wandelt einen Integer in ein Schichtkürzel um.
     * @param i Der gefundene Wert.
     * @return Die ermittelte Schicht.
     * @throws IllegalStateException Wird geworfen, wenn falscher Wert angegeben wird.
     */
    public static String getShiftName(int i) throws IllegalStateException {
        switch (i) {
            case 0:
                return "FS";
            case 1:
                return "SS";
            case 2:
                return "NS";
            default:
                throw new IllegalStateException("The integer has to be 0,1 or 2. Got: " + i);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui = new StartUI();
            }
        });

    }


    public static StartUI getUI() {
        return ui;
    }

    /**
     * Gibt den Wochenplan zurück. Kein Klon!
     * @return Der originale Wochenplan.
     */
    public static Wochenplan getWochenplan() {
        return wochenplan;
    }

    /**
     * Setzt den Wochenplan. Achtung: neuer Wochenplan löscht alle bisherigen Inhalte und startet das UI neu.
     * @param wp Der Wochenplan.
     */
    public static void setWochenplan(Wochenplan wp) {
        wochenplan = wp;
        ui.setVisible(false);
        ui = new StartUI();
    }
}
