package me.langner.jonas.wpapp.ui;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.DateField;
import me.langner.jonas.wpapp.objects.Period;
import me.langner.jonas.wpapp.objects.SelectionMonth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.peer.WindowPeer;
import java.text.ParseException;
import java.util.Date;

/**
 * Ist das FilterUI.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class MonthUI extends Frame {

    private JLabel title = new JLabel("Wählen Sie einen Monat aus", JLabel.CENTER);

    private JComboBox<SelectionMonth> select = new JComboBox<>(SelectionMonth.values());
    private DateField[] dateFields = new DateField[2];

    private JButton saveButton = new JButton("Speichern");

    public MonthUI() {
        super("Monat wählen", 600, 200);

        setResizable(false);

        title.setFont(title.getFont().deriveFont(Font.BOLD));

        createFields();
        setBounds();
        addListeners();

        addToPanel(
                title,
                select,
                dateFields[0],
                dateFields[1],
                saveButton
        );

        reload();
        setVisible(true);

        setAlwaysOnTop(true);
    }

    /**
     * Fügt Listener hinzu.
     */
    private void addListeners() {
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object object = select.getSelectedItem();

                if (object instanceof SelectionMonth && select.getSelectedIndex() > 0) {
                    // Monat wurde gewählt
                    SelectionMonth month = (SelectionMonth) object;

                    /* Datum anpassen */
                    dateFields[0].setText(WPAPP.DISPLAY_FORMAT.format(DateField.getFirstDayOfMonth(month.getMonth(),0)));
                    dateFields[1].setText(WPAPP.DISPLAY_FORMAT.format(DateField.getLastDayOfMonth(month.getMonth(),0)));
                }
            }
        });

        /* speichern */
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Date start = WPAPP.DISPLAY_FORMAT.parse(dateFields[0].getText());
                    Date end = WPAPP.DISPLAY_FORMAT.parse(dateFields[1].getText());

                    WPAPP.getWochenplan().setPeriod(new Period(start,end));


                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

            }
        });
    }

    /**
     * Erstellt die Datumsfelder.
     */
    private void createFields() {
        dateFields[0] = new DateField(DateField.DateType.START);
        dateFields[1] = new DateField(DateField.DateType.END);
    }

    /**
     * Setzt die Koordinaten der Objekte
     */
    private void setBounds() {
        title.setBounds(0,0, getWidth(), 30);

        select.setBounds(0,60,getWidth(),40);

        dateFields[0].setBounds(50,100,getWidth()-100, 40);
        dateFields[1].setBounds(50,150,getWidth()-100, 40);

        saveButton.setBounds(100,230,getWidth()-200,20);
    }

    @Override
    public void onResize(ComponentEvent event) {
        return;
    }
}
