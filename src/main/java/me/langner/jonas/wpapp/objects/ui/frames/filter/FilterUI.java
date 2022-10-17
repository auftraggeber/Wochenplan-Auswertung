package me.langner.jonas.wpapp.objects.ui.frames.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.filter.StaffEntryFilter;
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

    private final JButton openDateFilter = new JButton("Datumsfilter hinzufügen");
    private final JButton openMachineFilter = new JButton("Maschinenfilter hinzufügen");
    private final JButton openToolFilter = new JButton("Werkzeugfilter hinzufügen");

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
                openToolFilter
        );

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        openDateFilter.setBounds(465, 80, 225, 30);
        openMachineFilter.setBounds(465, 115, 225, 30);
        openToolFilter.setBounds(465, 150, 225, 30);
        resetButton.setBounds(465, 320, 225, 40);
    }

    private void initButtons() {
        final Frame frame = this;
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StaffEntryFilter.reset();
                reloadFilters();
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
    }

    @Override
    public void onResize(ComponentEvent event) {
        return;
    }
}
