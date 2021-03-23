package me.langner.jonas.wpapp.objects.ui.frames;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.time.Period;
import me.langner.jonas.wpapp.objects.time.SelectionMonth;
import me.langner.jonas.wpapp.objects.ui.elements.DateField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;

/**
 * Ist das FilterUI.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class FilterUI extends Frame {

    private JLabel
            selectTitle = new JLabel("Wählen Sie einen Monat aus", JLabel.CENTER),
            dateTitle = new JLabel("Geben Sie einen Zeitraum ein", JLabel.CENTER),
            dateFrom = new JLabel("Von: "),
            dateTo = new JLabel("Bis: ");

    private JComboBox<SelectionMonth> select = new JComboBox<>(SelectionMonth.values());
    private DateField[] dateFields = new DateField[2];

    private JButton
            saveButton = new JButton("Speichern"),
            resetButton = new JButton("Zurücksetzten");

    private JPanel
            selectPanel = new JPanel(),
            datePanel = new JPanel();

    /**
     * Erstellt ein neues Fenster.
     */
    public FilterUI() {
        super("Filter", 600, 200);

        setResizable(false);

        selectTitle.setFont(selectTitle.getFont().deriveFont(Font.BOLD));
        dateTitle.setFont(selectTitle.getFont());

        selectPanel.setBackground(Color.WHITE);
        selectPanel.setLayout(null);

        datePanel.setBackground(Color.WHITE);
        datePanel.setLayout(null);

        dateFields[0] = new DateField(DateField.DateType.START);
        dateFields[1] = new DateField(DateField.DateType.END);

        dateFields[0].setBackground(new Color(230,230,230));
        dateFields[1].setBackground(dateFields[0].getBackground());

        setBounds();
        addListeners();

        selectPanel.add(selectTitle);
        selectPanel.add(select);

        datePanel.add(dateTitle);
        datePanel.add(dateFields[0]);
        datePanel.add(dateFields[1]);
        datePanel.add(dateFrom);
        datePanel.add(dateTo);


        addToPanel(
                selectPanel,
                datePanel,
                saveButton,
                resetButton
        );

        reload();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

                    /* überprüfen, ob Sinnvolle Eingabe */
                    if (end.before(start) && ! end.equals(start)) {
                        // keinen Sinn -> ausgeben und anpassen
                        new ErrorUI(
                                "Fehlerhafte Eingabe! Die eingegebenen Daten ergaben keinen Sinn. Enddatum wurde angepasst!",
                                new IllegalArgumentException("start date is after end date")
                        );

                        end = start;
                    }

                    WPAPP.getWochenplan().setPeriod(new Period(start,end));
                    WPAPP.getUI().reloadInformation();

                    setVisible(false);


                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateFields[0].setDefaultDate();
                dateFields[1].setDefaultDate();

                select.setSelectedIndex(0);
            }
        });
    }

    /**
     * Setzt die Koordinaten der Objekte
     */
    private void setBounds() {
        selectPanel.setBounds(10,10,getWidth()-20,75);
        datePanel.setBounds(10,95,getWidth()-20,113);

        selectTitle.setBounds(0,0, selectPanel.getWidth(), 30);

        select.setBounds(50,30,selectPanel.getWidth() - 100,25);

        dateFields[0].setBounds(150,30,datePanel.getWidth()-300, 23);
        dateFields[1].setBounds(150,70,datePanel.getWidth()-300, 23);

        dateFrom.setBounds(100,30,50,23);
        dateTo.setBounds(100,70,50,23);

        dateTitle.setBounds(0,0, datePanel.getWidth(), 30);

        saveButton.setBounds(100,215,getWidth()/2 - 110,50);
        resetButton.setBounds(getWidth()/2 + 10, 215, saveButton.getWidth(), 50);
    }

    @Override
    public void onResize(ComponentEvent event) {
        return;
    }
}
