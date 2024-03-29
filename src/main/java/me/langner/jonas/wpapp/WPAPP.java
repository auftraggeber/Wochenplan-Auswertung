package me.langner.jonas.wpapp;

import me.langner.jonas.wpapp.objects.*;
import me.langner.jonas.wpapp.objects.ui.frames.ErrorUI;
import me.langner.jonas.wpapp.objects.ui.frames.StartUI;
import me.langner.jonas.wpapp.objects.ui.frames.WPUI;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

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
    public static final String VERSION = "pre 1.1.0";
    public static final int BUILD = 23;
    public static final String SINCE = "2022/10";

    private static Wochenplan wochenplan = new Wochenplan();
    private static WPUI ui;
    private static StartUI startUI;

    /**
     * Wandelt einen Integer in ein Schichtkürzel um.
     * @param i Der gefundene Wert.
     * @return Die ermittelte Schicht.
     * @throws IllegalStateException Wird geworfen, wenn falscher Wert angegeben wird.
     */
    public static String getShiftName(int i) throws IllegalStateException {
        switch (i) {
            case SHIFT_EARLY:
                return "FS";
            case SHIFT_LATE:
                return "SS";
            case SHIFT_NIGHT:
                return "NS";
            default:
                throw new IllegalStateException("The integer has to be " + SHIFT_EARLY + "," + SHIFT_LATE + " or " + SHIFT_NIGHT + ". Got: " + i);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            startUI = new StartUI();
        }catch (FileNotFoundException ex) {
            new ErrorUI("Programm möglicherweise unvollständig", ex);
        }
        catch (RuntimeException ex) {
            new ErrorUI("Es ist ein unerwarteter, nicht behandelter Fehler aufgetreten. Das Programm versucht diesen zu übergehen.", ex);
        }

        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ui = new WPUI();
                        }
                        catch(RuntimeException ex) {
                            new ErrorUI("Es ist ein unerwarteter, nicht behandelter Fehler aufgetreten. Das Programm versucht diesen zu übergehen.", ex);
                        }

                    }
                });
            }
        }, 3750);


    }


    public static WPUI getUI() {
        return ui;
    }

    public static StartUI getStartUI() {
        return startUI;
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
        ui = new WPUI();
    }
}
