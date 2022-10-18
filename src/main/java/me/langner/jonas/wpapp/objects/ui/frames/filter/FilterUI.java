package me.langner.jonas.wpapp.objects.ui.frames.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.filter.*;
import me.langner.jonas.wpapp.objects.settings.Setting;
import me.langner.jonas.wpapp.objects.ui.frames.ErrorUI;
import me.langner.jonas.wpapp.objects.ui.frames.Frame;
import me.langner.jonas.wpapp.objects.ui.frames.filter.DateFilterUI;
import me.langner.jonas.wpapp.objects.ui.frames.filter.FactoryElementFilterUI;
import me.langner.jonas.wpapp.objects.ui.frames.filter.strategy.MachineFilterUIStrategyImpl;
import me.langner.jonas.wpapp.objects.ui.frames.filter.strategy.ToolFilterUIStrategyImpl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.Vector;

public class FilterUI extends Frame {

    private final JList<StaffEntryFilter> filterStackList = new JList<>();
    private final JScrollPane scrollPane = new JScrollPane(filterStackList);

    private final JLabel filterTitleLabel = new JLabel("", JLabel.CENTER);
    private final JTextPane filterDescriptionPane = new JTextPane();
    private Vector<StaffEntryFilter> data = null;

    private final JButton saveButton = new JButton("Filter speichern");
    private final JButton loadButton = new JButton("Filter laden");
    private final JButton resetButton = new JButton("Filter zurücksetzen");

    private final JButton useFilterButton = new JButton("Filter anwenden");

    private final JButton openDateFilter = new JButton("Datumsfilter hinzufügen");
    private final JButton openMachineFilter = new JButton("Maschinenfilter hinzufügen");
    private final JButton openToolFilter = new JButton("Werkzeugfilter hinzufügen");

    private final JButton editButton = new JButton("Komponente bearbeiten");

    public FilterUI() {
        super("Filter", 700, 400);
        setLayout(null);

        filterTitleLabel.setFont(filterTitleLabel.getFont().deriveFont(20F));

        filterDescriptionPane.setEditable(false);

        filterStackList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                final int index = filterStackList.getSelectedIndex();
                if (index >= 0) {
                    filterDescriptionPane.setText(data.get(index).getLongDescription());
                }
                else filterDescriptionPane.setText("");
            }
        });

        setBounds();
        initButtons();
        reloadFilters();

        addToPanel(
                scrollPane,
                filterTitleLabel,
                filterDescriptionPane,
                saveButton,
                loadButton,
                resetButton,
                openDateFilter,
                openMachineFilter,
                openToolFilter,
                editButton,
                useFilterButton
        );

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setResizable(false);
        setAlwaysOnTop(true);
        open();
    }

    private void setBounds() {
        filterTitleLabel.setBounds(5, 5, 450, 50);
        scrollPane.setBounds(5, 60, 240, 300);
        filterDescriptionPane.setBounds(255, 60, 200, 300);


        saveButton.setBounds(465, 5, 225, 30);
        loadButton.setBounds(465, 40, 225, 30);

        useFilterButton.setBounds(465, 80, 225, 30);

        openDateFilter.setBounds(465, 170, 225, 30);
        openMachineFilter.setBounds(465, 205, 225, 30);
        openToolFilter.setBounds(465, 240, 225, 30);

        editButton.setBounds(465, 280, 225, 30);

        resetButton.setBounds(465, 320, 225, 40);
    }

    private void initButtons() {
        final Frame frame = this;
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (StaffEntryFilter.getActive().persist()) {
                    dispose();
                }
                else {
                    new ErrorUI("Es gab einen Fehler beim Speichern.", new Exception("Der Filter konnte nicht gesichert werden. Versuchen Sie es erneut."));
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoadFilterUI();
                dispose();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StaffEntryFilter.reset();
                reloadFilters();
                WPAPP.getUI().updateLists();
                WPAPP.getUI().reloadInformation();
            }
        });

        openDateFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DateFilterUI();
                frame.dispose();
            }
        });

        openMachineFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FactoryElementFilterUI(new MachineFilterUIStrategyImpl());
                frame.dispose();
            }
        });

        openToolFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FactoryElementFilterUI(new ToolFilterUIStrategyImpl());
                frame.dispose();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StaffEntryFilter filter = filterStackList.getSelectedValue();

                if (filter != null) {
                    if (filter instanceof MachineFilter) {
                        new FactoryElementFilterUI(new MachineFilterUIStrategyImpl((MachineFilter) filter));
                        dispose();
                        return;
                    }
                    if (filter instanceof ToolFilter) {
                        new FactoryElementFilterUI(
                                new ToolFilterUIStrategyImpl((ToolFilter) filter)
                        );
                        dispose();
                        return;
                    }
                    if (filter instanceof StartFilter || filter instanceof EndFilter) {
                        new DateFilterUI(filter.getFirstFilterOfType(StartFilter.class), filter.getFirstFilterOfType(EndFilter.class));
                        dispose();
                    }
                }
            }
        });

        useFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void reloadFilters() {
        filterTitleLabel.setText(StaffEntryFilter.getActive().getName());
        data = StaffEntryFilter.getActive().getFilterStack();

        filterStackList.setListData(data);

        if (!data.isEmpty()) {
            final int selectedIndex = data.size() - 1;
            filterStackList.setSelectedIndex(selectedIndex);
        }
        else filterStackList.setSelectedIndices(null);

        openDateFilter.setEnabled(StaffEntryFilter.getActive().getFirstFilterOfType(StartFilter.class) == null && StaffEntryFilter.getActive().getFirstFilterOfType(EndFilter.class) == null);
        openMachineFilter.setEnabled(StaffEntryFilter.getActive().getFirstFilterOfType(MachineFilter.class) == null);
        openToolFilter.setEnabled(StaffEntryFilter.getActive().getFirstFilterOfType(ToolFilter.class) == null);
    }

    @Override
    public void onResize(ComponentEvent event) {
        return;
    }
}
