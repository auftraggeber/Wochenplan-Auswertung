package me.langner.jonas.wpapp;

import me.langner.jonas.wpapp.listener.FactoryChangeListener;
import me.langner.jonas.wpapp.objects.*;
import me.langner.jonas.wpapp.ui.Frame;
import me.langner.jonas.wpapp.ui.StartUI;
import me.langner.jonas.wpapp.xml.WochenplanFileReader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Die Hauptklasse: von hier startet das Program.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class WPAPP {

    public static final Wochenplan WOCHENPLAN = new Wochenplan();
    public static final SimpleDateFormat
            SQL_FORMAT = new SimpleDateFormat("yyyy-MM-dd"),
            DISPLAY_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final int SHIFT_EARLY = 0, SHIFT_LATE = 1, SHIFT_NIGHT = 2;

    public static String getShiftName(int i) {
        switch (i) {
            case 0:
                return "FS";
            case 1:
                return "SS";
            case 2:
                return "NS";
            default:
                return "--";
        }
    }

    public static void main(String[] args) {
        new StartUI();
    }
}
