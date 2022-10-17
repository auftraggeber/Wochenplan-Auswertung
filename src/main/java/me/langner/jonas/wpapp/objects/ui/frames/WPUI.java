package me.langner.jonas.wpapp.objects.ui.frames;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.listener.FactoryChangeListener;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.Wochenplan;
import me.langner.jonas.wpapp.objects.ui.frames.filter.FilterUI;
import me.langner.jonas.wpapp.xml.WochenplanFileReader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Ist das HauptUI.
 * @author Jonas Langner
 * @version 1.0.1
 * @since Alpha
 */
public class WPUI extends Frame {

    private String[] tableHeaders = new String[]{
            "Datum",
            "Schicht",
            "Maschine",
            "Werkzeug",
            "Werker",
            "Rüstung"
    };
    private List<StaffEntry> lastEntries;
    private boolean autoChanged = false;

    private JLabel machineTitle = new JLabel("Maschinen", JLabel.CENTER);
    private JLabel toolTitle = new JLabel("Werkzeuge", JLabel.CENTER);
    private JLabel informationTitle = new JLabel("Informationen", JLabel.CENTER);

    private JList machineList = new JList(new String[0]);
    private JScrollPane machineScrollPane = new JScrollPane(machineList);

    private JList toolList = new JList(new String[0]);
    private JScrollPane toolScrollPane = new JScrollPane(toolList);

    private JPanel informationPanel = new JPanel();
    private JLabel topInfoText = new JLabel("");
    private JLabel sumInfoText[] = new JLabel[2];

    private JButton resetMachine = new JButton("Alles abwählen");
    private JButton resetTool = new JButton("Alles abwählen");

    private JTable table = new JTable(new Object[0][] , tableHeaders);
    private JScrollPane tableScrollPane = new JScrollPane(table);
    private JPanel tablePanel = new JPanel();

    private JCheckBox onlyWithData = new JCheckBox("Nullwerte ausblenden");

    private JButton moreData = new JButton("Weitere Datei einbinden");

    private JLabel selectedSumLabel[] = new JLabel[2];
    private JPanel selectedSumPanel = new JPanel();

    private Menu menu = new Menu("Aktionen");
    private MenuItem menuItem = new MenuItem("Zurücksetzen");
    private MenuItem menuItemMonthSelect = new MenuItem("Filter anzeigen");

    private Runnable reloadRunnable;

    public WPUI() {
        super("WPAPP Auswertung - Version " + WPAPP.VERSION, 1500, 1300);

        setMinWidth(1100);

        /* Stile setzen */
        machineTitle.setFont(machineTitle.getFont().deriveFont(Font.BOLD));
        toolTitle.setFont(toolTitle.getFont().deriveFont(Font.BOLD));
        informationTitle.setFont(informationPanel.getFont().deriveFont(Font.BOLD));

        machineScrollPane.setBackground(Color.WHITE);

        toolScrollPane.setBackground(Color.WHITE);

        informationPanel.setBackground(Color.WHITE);
        informationPanel.setLayout(new FlowLayout());

        tablePanel.setLayout(new BorderLayout());
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setBackground(new Color(225,225,225));
        tableScrollPane.setBounds(10,120, tablePanel.getWidth() - 20, tablePanel.getHeight() - 130);
        tablePanel.add(tableScrollPane);

        selectedSumPanel.setBackground(Color.WHITE);
        selectedSumPanel.setLayout(null);
        selectedSumLabel[0] = new JLabel("Ausgewähltes Personal: 0.0");
        selectedSumLabel[1] = new JLabel("Ausgewählte Rüstzeit: 0.0h");
        selectedSumPanel.add(selectedSumLabel[0]);
        selectedSumPanel.add(selectedSumLabel[1]);

        onlyWithData.setSelected(true);

        buildMenu();
        addInformationElements();
        setBounds();

        /* zu frame hinzufügen */
        addToPanel(
                machineTitle,
                toolTitle,
                informationTitle,
                machineScrollPane,
                toolScrollPane,
                informationPanel,
                resetMachine,
                resetTool,
                tablePanel,
                onlyWithData,
                moreData,
                selectedSumPanel
        );

        /* events hinzufügen */
        addListeners();

        /* Datei auswählen */
        new WochenplanFileReader();

        open();
        /* anzeigen */
        reload();
    }

    /**
     * Öffnet das Fenster.
     */
    @Override
    public void open() {
        super.open();

        if (WPAPP.getStartUI() != null)
            WPAPP.getStartUI().setVisible(false);
    }

    /**
     * Baut das Menü.
     */
    private void buildMenu() {
        setMenuBar(new MenuBar());
        menu.add(menuItem);
        menu.add(menuItemMonthSelect);
        getMenuBar().add(menu);

        /* shortcuts hinzufügen */
        menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_R,false));
        menuItemMonthSelect.setShortcut(new MenuShortcut(KeyEvent.VK_F, true));

        /* beim klicken neuen Wochenplan erstellen */
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WPAPP.setWochenplan(new Wochenplan());
            }
        });

        menuItemMonthSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FilterUI();
            }
        });
    }

    /**
     * Passt Elemente von ihrer Größe an.
     */
    private void setBounds() {
        machineTitle.setBounds(10,0,250,60);
        toolTitle.setBounds(270,0,250,60);
        informationTitle.setBounds(530, 0, getWidth()-520, 60);

        machineScrollPane.setBounds(10,40, 250, getHeight() - 190);

        toolScrollPane.setBounds(270, 40, 250, getHeight() - 190);

        informationPanel.setBounds(540, 40, getWidth()-560, 70);

        resetMachine.setBounds(10, getHeight() - 147, 250, 45);
        resetTool.setBounds(270, getHeight() - 147, 250, 45);

        tablePanel.setBounds(540, 115, getWidth()-560, getHeight() - 220);

        onlyWithData.setBounds(540, getHeight() - 100, 200, 30);

        moreData.setBounds(10, getHeight()-100, 510, 30);

        selectedSumPanel.setBounds( 750, getHeight() - 100, getWidth() - 770, 40);
        selectedSumLabel[0].setBounds(0,0, selectedSumPanel.getWidth(), 20);
        selectedSumLabel[1].setBounds(0,20, selectedSumPanel.getWidth(), 20);

        topInfoText.setBounds(10,10, informationPanel.getWidth() - 20, 20);
        sumInfoText[0].setBounds(10, 30, informationPanel.getWidth() - 20, 20);
        sumInfoText[1].setBounds(10, 50, informationPanel.getWidth() - 20, 20);
    }

    /**
     * Fügt Events/Listener hinzu.
     */
    private void addListeners() {
        /* auf updates überprüfen */
        WPAPP.getWochenplan().addListener(new FactoryChangeListener() {
            @Override
            public void machineAdded(Machine machine) {
                updateList(machineList, WPAPP.getWochenplan().getMachines().toArray());
            }

            @Override
            public void toolAdded(Tool tool) {
                updateList(toolList, WPAPP.getWochenplan().getTools().toArray());
            }

            @Override
            public void machineRemoved(Machine machine) {
                updateList(machineList, WPAPP.getWochenplan().getMachines().toArray());
            }

            @Override
            public void toolRemoved(Tool tool) {
                updateList(toolList, WPAPP.getWochenplan().getTools().toArray());
            }
        });

        /* überprüfen, ob neues selektiert (Maschine) */
        machineList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                machineSelectionChanged();
            }
        });

        /* überprüfen, ob neues selektiert (Werkzeug) */
        toolList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!autoChanged) {
                    resetSelection(machineList);
                    toolSelectionChanged();
                }

            }
        });

        /* überprüfen, ob Selektion löschen (Maschine) */
        resetMachine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSelection(machineList);
                toolSelectionChanged();
            }
        });

        /* überprüfen, ob Selektion löschen (Werkzeug) */
        resetTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSelection(toolList);
                machineSelectionChanged();
            }
        });

        /* überprüfen, ob Check geklickt wurde */
        onlyWithData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildTable();
            }
        });

        /* ob noch eine Datei geöffnet werden soll */
        moreData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WochenplanFileReader();

                reload();
            }
        });

        /* schauen, ob reihen ausgewählt wurden (Tabelle) */
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateSelectedTotals();
            }
        });
    }

    /**
     * Wird aufgerufen, wenn sich die Auswahl der Maschinen geändert hat.
     */
    private void machineSelectionChanged() {
        java.util.List<String> selection = machineList.getSelectedValuesList();
        java.util.List<StaffEntry> entries = new ArrayList<>();
        java.util.List<Integer> newSelection = new ArrayList<>();

        /* alle einträge hinzufügen */
        selection.forEach((selectedName) -> {
            Machine machine = WPAPP.getWochenplan().getMachineByName(selectedName);

            /* alle zugehörigen Tools selektieren */
            machine.getTools().forEach((tool) -> {

                /* überprüfen, ob Tool dabei ist */
                for (int i = 0; i < toolList.getModel().getSize(); i++) {
                    if (toolList.getModel().getElementAt(i).equals(tool.getName())) {
                        // Tool ist dabei - zur neuen Auswahl hinzufügen
                        if (!newSelection.contains(i))
                            newSelection.add(i);
                        break;
                    }
                }
            });

            /* da maschine ausgewählt auch nur daten, die mit der Maschine in verbindung stehen, verwenden */
            machine.getEntries().forEach((entry) -> {
                if (!entries.contains(entry))
                    entries.add(entry);
            });
        });

        /* in array umwandeln */
        int[] newArray = new int[newSelection.size()];

        for (int i = 0; i < newSelection.size(); i++) {
            newArray[i] = newSelection.get(i);
        }

        /* selektieren */
        if (machineList.getSelectedIndices().length > 0) {
            autoChanged = true;
            toolList.setSelectedIndices(newArray);
            autoChanged = false;
        }

        /* updaten */
        showInformation(entries);

        /* für reload festlegen */
        reloadRunnable = new Runnable() {
            @Override
            public void run() {
                machineSelectionChanged();
            }
        };
    }

    /**
     * Wird aufgerufen, wenn sich die Auswahl der Werkzuege geändert hat.
     */
    private void toolSelectionChanged() {
        java.util.List<String> selection = toolList.getSelectedValuesList();
        java.util.List<StaffEntry> entries = new ArrayList<>();

        /* alle einträge hinzufügen */
        selection.forEach((selectedName) -> {
            Tool tool = WPAPP.getWochenplan().getToolByName(selectedName);

            tool.getEntries().forEach((entry) -> {
                if (!entries.contains(entry))
                    entries.add(entry);
            });
        });

        /* updaten */
        showInformation(entries);

        /* für reload festlegen */
        reloadRunnable = new Runnable() {
            @Override
            public void run() {
                toolSelectionChanged();
            }
        };
    }

    /**
     * Setzt die Auswahl einer JList zurück
     * @param list Die Liste.
     */
    private void resetSelection(JList list) {
        list.setSelectedIndices(new int[0]);
    }

    /**
     * Fügt Elemente zu einem Panel hinzu.
     */
    private void addInformationElements() {

        /* Stil setzen */
        sumInfoText[0] = new JLabel();
        sumInfoText[1] = new JLabel();

        /* hinzufügen */
        informationPanel.add(topInfoText);
        informationPanel.add(sumInfoText[0]);
        informationPanel.add(sumInfoText[1]);
    }

    /**
     * Setzt neue Werte in eine JList.
     * @param list Die JList, die geändert werden soll.
     * @param elementList Liste mit neuen Werten.
     */
    private void updateList(JList list, Object[] elementList) {
        /* anzeige laden */
        if (!isVisible())
            open();

        String[] content = new String[elementList.length];

        /* Zu Array konvertieren */
        int i = 0;
        for (Object element : elementList) {
            content[i] = element.toString();
            i++;
        }

        /* setzen */
        list.setListData(content);

        /* neu laden */
        reload();

        /* mit 50ms Verzögerung neu laden */
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /* neu laden */
                reload();
            }
        },50);
    }

    /**
     * Lädt die Informationen neu.
     */
    public void reloadInformation() {
        if (reloadRunnable != null)
            reloadRunnable.run();
    }

    /**
     * Lädt die Infos neu.
     * @param entries Liste der gewählten Einträge.
     */
    private void showInformation(List<StaffEntry> entries) {
        topInfoText.setText("Sie haben " + entries.size() + " Einträge gewählt.");

        /* Summe bilden */
        float sum = 0;
        float preparationTimeSum = 0F;
        int preparations = 0;
        for (StaffEntry entry : entries) {
            sum += entry.getValue();

            if (entry.hasPreparation()) {
                preparationTimeSum += entry.getTool().getPreparationTime();
                preparations++;
            }
        }

        /* in Stunden zusammenfassen */
        preparationTimeSum = preparationTimeSum / 60F;

        sumInfoText[0].setText("Im Zeitraum vom " + WPAPP.getWochenplan().getFilterPeriod().getStartDisplay() + " bis " + WPAPP.getWochenplan().getFilterPeriod().getEndDisplay()
                + " standen " + sum + " Werker an dieser Auswahl.");
        sumInfoText[1].setText("Die Rüstzeit betrug " + preparationTimeSum + " Stunden. Es gab insgesamt " + preparations + " Rüstungen.");

        /* speichern für neuladen */
        lastEntries = entries;

        buildTable();
    }

    /**
     * Erstellt die Tabelle.
     */
    private void buildTable() {
        List<StaffEntry> entries = (lastEntries != null && !onlyWithData.isSelected()) ? lastEntries : new ArrayList<>();

        /* überprüfen, ob gekürzt werden muss */
        if (onlyWithData.isSelected() && lastEntries != null) {
            // muss gekürzt werden

            for (StaffEntry entry : lastEntries) {

                /* wenn kein Wert -> ausblenden */
                if (entry.getValue() > 0 || entry.hasPreparation()) {
                    // ausblenden
                    entries.add(entry);
                }
            }
        }

        /* Reihen für Tabelle erstellen */
        Object[][] rows = new Object[entries.size()][5];

        /* Reihen lesen & Daten eintragen */
        for (int i = 0; i < entries.size(); i++) {
            StaffEntry entry = entries.get(i);

            /* Reihe erstellen */
            try {
                rows[i] = new Object[] {
                        WPAPP.DISPLAY_FORMAT.format(entry.getDate()),
                        WPAPP.getShiftName(entry.getShift()),
                        entry.getMachine().getName(),
                        entry.getTool().getName(),
                        entry.getValue(),
                        getPreparationDisplay(entry)
                };
            }
            catch (IllegalStateException ex) {
                new ErrorUI("Interner Fehler", ex);
            }

        }

        /* Daten zurücksetzen */
        DefaultTableModel model = new DefaultTableModel();

        /* Daten in Tabelle einfügen */
        model.setDataVector(rows, tableHeaders);

        /* änderungen anwenden */
        table.setModel(model);
        table.setRowSorter(new TableRowSorter<>(model));

        /* Daten neu laden */
        reload();
    }

    /**
     * Berechnet die ausgewählten Summen und zeigt sie an.
     */
    private void updateSelectedTotals() {
        int rows[] = table.getSelectedRows();

        float totalStaff = 0F;
        BigDecimal preparationSum = new BigDecimal(0.0);
        preparationSum.setScale(2);

        /* für jede Reihe Daten auslesen */
        for (int row : rows) {
            /*
            wichtig: sortierung beachten!

            mit convertRowIndexToModel werden die richtigen indices ausgegeben
             */

            /* Personal auslesen */
            float staff = (float) table.getModel().getValueAt(table.getRowSorter().convertRowIndexToModel(row), 4);
            totalStaff += staff;

            /* Rüstzeit auslesen */
            String preparation = (String) table.getModel().getValueAt(table.getRowSorter().convertRowIndexToModel(row), 5);
            try {
                preparationSum = preparationSum.add(new BigDecimal(String.valueOf(preparation.replaceAll("h",""))));
            }
            catch (Exception ex) {}
        }

        /* anzeigen */
        selectedSumLabel[0].setText("Ausgewähltes Personal: " + totalStaff);
        selectedSumLabel[1].setText("Ausgewählte Rüstzeit: " + preparationSum.toString() + "h");
    }

    /**
     * Ermittelt, wie die Rüstung angezeigt wird.
     * @param entry Der Eintrag mit Rüstung.
     * @return Die Anzeige.
     */
    private String getPreparationDisplay(StaffEntry entry) {

        /* ermitteln, ob Rüstung */
        if (entry.hasPreparation()) {
            // Rüstzeit ermitteln
            BigDecimal timeInHours = new BigDecimal(String.valueOf((float) entry.getTool().getPreparationTime() / 60F));

            /* ausgabe */
            return timeInHours + "h";
        }
        return "-----";
    }

    @Override
    public void onResize(ComponentEvent event) {
        setBounds();
        reload();
    }
}
