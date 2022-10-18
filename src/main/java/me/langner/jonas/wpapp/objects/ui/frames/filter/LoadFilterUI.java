package me.langner.jonas.wpapp.objects.ui.frames.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.filter.StaffEntryFilter;
import me.langner.jonas.wpapp.objects.settings.Setting;
import me.langner.jonas.wpapp.objects.ui.frames.Frame;
import me.langner.jonas.wpapp.objects.ui.frames.filter.listener.OpenFilterUIOnWindowCloseListenerImpl;

import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * UI zum Laden von Filtern.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 18.10.22
 */
public class LoadFilterUI extends Frame {

    private final JList<String> filterNameList = new JList<>();
    private final JButton button = new JButton("Filter laden");

    /**
     * Baut die Komponenten zusammen.
     */
    public LoadFilterUI() {
        super("Filter laden", 600, 400);

        filterNameList.setBounds(5,5,590,300);
        button.setBounds(5, 305, 590, 30);

        addToPanel(filterNameList, button);

        setResizable(false);
        setAlwaysOnTop(true);

        loadListItems();
        initButton();

        addWindowListener(new OpenFilterUIOnWindowCloseListenerImpl());

        open();
    }

    /**
     * Lädt die verfügbaren Filter in die Liste.
     */
    private void loadListItems() {
        Vector<String> names = new Vector<>(Setting.getPersistedFilterNames());
        filterNameList.setListData(names);
    }

    /**
     * Lädt die Aktion des Buttons.
     */
    private void initButton() {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = filterNameList.getSelectedValue();

                if (selectedName != null) {
                    StaffEntryFilter.loadFilterFromFile(selectedName);
                    WPAPP.getUI().reloadInformation();
                    WPAPP.getUI().updateLists();
                    dispose();
                }
            }
        });
    }

    @Override
    public void onResize(ComponentEvent event) {
        return;
    }
}
