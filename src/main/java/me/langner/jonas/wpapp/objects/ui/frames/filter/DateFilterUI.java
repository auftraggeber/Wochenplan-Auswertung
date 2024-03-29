package me.langner.jonas.wpapp.objects.ui.frames.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.filter.EndFilter;
import me.langner.jonas.wpapp.objects.filter.StaffEntryFilter;
import me.langner.jonas.wpapp.objects.filter.StartFilter;
import me.langner.jonas.wpapp.objects.time.SelectionMonth;
import me.langner.jonas.wpapp.objects.ui.elements.DateField;
import me.langner.jonas.wpapp.objects.ui.frames.ErrorUI;
import me.langner.jonas.wpapp.objects.ui.frames.Frame;
import me.langner.jonas.wpapp.objects.ui.frames.filter.listener.OpenFilterUIOnWindowCloseListenerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Date;

/**
 * Ist das FilterUI.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class DateFilterUI extends Frame {

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

    private StartFilter startFilter;
    private EndFilter endFilter;

    /**
     * Erstellt ein neues Fenster.
     */
    public DateFilterUI() {
        this(null, null);
    }

    public DateFilterUI(final StartFilter startFilter, final EndFilter endFilter) {
        super("Filter", 600, 200);
        this.startFilter = startFilter;
        this.endFilter = endFilter;

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

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        addWindowListener(new OpenFilterUIOnWindowCloseListenerImpl());

        open();
        if (startFilter != null)
            dateFields[0].setValue(startFilter.getStart());
        if (endFilter != null)
            dateFields[1].setValue(endFilter.getEnd());
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
                    dateFields[0].setEdited(true);
                    dateFields[1].setEdited(true);
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
                        dateFields[1].setEdited(true);
                    }

                    if (startFilter == null)
                        startFilter = StaffEntryFilter.getActive().getFirstFilterOfType(StartFilter.class);

                    if (endFilter == null)
                        endFilter = StaffEntryFilter.getActive().getFirstFilterOfType(EndFilter.class);

                    if (dateFields[1].wasEdited()) {
                        if (endFilter != null) {
                            endFilter.setEnd(end);
                        }
                        else {
                            endFilter = new EndFilter(end);
                        }
                    }

                    if (dateFields[0].wasEdited()) {
                        if (startFilter != null) {
                            startFilter.setStart(start);
                        }
                        else {
                            startFilter = new StartFilter(start);
                        }
                    }

                    WPAPP.getUI().reloadInformation();

                    dispose();

                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateFields[0].setDefaultDate(true);
                dateFields[1].setDefaultDate(true);
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
