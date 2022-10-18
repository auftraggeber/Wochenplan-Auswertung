package me.langner.jonas.wpapp.objects.ui.frames.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.FactoryElement;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.filter.MachineFilter;
import me.langner.jonas.wpapp.objects.filter.StaffEntryFilter;
import me.langner.jonas.wpapp.objects.filter.ToolFilter;
import me.langner.jonas.wpapp.objects.ui.frames.Frame;
import me.langner.jonas.wpapp.objects.ui.frames.filter.listener.OpenFilterUIOnWindowCloseListenerImpl;
import me.langner.jonas.wpapp.objects.ui.frames.filter.strategy.IFactoryElementFilterUIStrategy;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Das UI für {@link MachineFilter} und {@link ToolFilter}.
 * Zwei Listen, die angeben, ob die Elemente im Filter sind oder nicht.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public class FactoryElementFilterUI extends Frame {

    private final IFactoryElementFilterUIStrategy strategy;

    private final JList<FactoryElement> toAddList = new JList<>(), addedList = new JList<>();

    private final JButton addButton = new JButton(">>>"), removeButton = new JButton("<<<");
    private final JButton saveButton = new JButton("Speichern");

    /**
     * Erstellt ein neues UI.
     * @param strategy Die Versorgung mit Informationen.
     */
    public FactoryElementFilterUI(IFactoryElementFilterUIStrategy strategy) {
        super(strategy.getTitle(), 600, 400);
        this.strategy = strategy;


        setLayout(null);

        JScrollPane toAddPane = new JScrollPane(toAddList);
        toAddPane.setBounds(5, 35, 260, 290);

        JScrollPane addedPane = new JScrollPane(addedList);
        addedPane.setBounds(337, 35, 260, 290);

        JLabel toAddLabel = new JLabel("Vom Filter NICHT zugelassen");
        toAddLabel.setBounds(5,5,260,25);
        JLabel addedLabel = new JLabel("Zugelassen");
        addedLabel.setBounds(340, 5, 260, 25);

        addButton.setBounds(265, 130, 70, 30);
        removeButton.setBounds(265,170, 70, 30);

        saveButton.setBounds(5, 330, 590, 30);

        initButtons();

        addToPanel(toAddPane, addedPane, toAddLabel, addedLabel, addButton, removeButton, saveButton);

        refreshLists();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        addWindowListener(new OpenFilterUIOnWindowCloseListenerImpl());

        open();
    }

    /**
     * Lädt beide Listen neu.
     */
    private void refreshLists() {
        toAddList.setListData(strategy.getElementsToAdd());
        addedList.setListData(strategy.getAddedElements());
    }

    /**
     * Setzt die Listener der Buttons.
     */
    private void initButtons() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (strategy.addElements(toAddList.getSelectedValuesList()))
                    refreshLists();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (strategy.removeElements(addedList.getSelectedValuesList()))
                    refreshLists();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strategy.save();
                WPAPP.getUI().updateLists();
                WPAPP.getUI().reloadInformation();
                dispose();
            }
        });
    }


    @Override
    public void onResize(ComponentEvent event) {
        return;
    }
}
