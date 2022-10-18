package me.langner.jonas.wpapp.objects;

import me.langner.jonas.wpapp.objects.filter.*;
import me.langner.jonas.wpapp.objects.listener.FactoryChangeListener;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.time.Period;

import java.util.*;

/**
 * Der Inhalt des Wochenplans wird hier gespeichert.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class Wochenplan {

    private Period filterPeriod, xmlPeriod;
    private Map<Integer, Machine> machines = new HashMap<>();
    private Map<Integer, Tool> tools = new HashMap<>();
    private Set<FactoryChangeListener> listeners = new HashSet<>();

    private List<Machine> filteredMachines = null;
    private List<Tool> filteredTools = null;

    public void resetAllFilters() {
        clearFilterPeriod();
        resetFilteredMachines();
        resetFilteredTools();
    }

    public void clearFilterPeriod() {
        filterPeriod = null;
    }

    public void resetFilteredMachines() {
        filteredMachines = null;

        if (StaffEntryFilter.getActive() != null) {
            MachineFilter filter = StaffEntryFilter.getActive().getFirstFilterOfType(MachineFilter.class);

            if (filter != null) {
                filteredMachines = filter.getFilteredMachines();
            }
        }
    }

    public void resetFilteredTools() {
        filteredTools = null;

        if (StaffEntryFilter.getActive() != null) {
            ToolFilter filter = StaffEntryFilter.getActive().getFirstFilterOfType(ToolFilter.class);

            if (filter != null) {
                filteredTools = filter.getFilteredTools();
            }
        }
    }

    public void setPeriod(Period period) {
        this.xmlPeriod = period;
    }

    /**
     * Fügt eine Maschine hinzu. Alle Werkzeuge der Maschine werden ebenso hinzugefügt.
     * @param machine Die Maschine
     */
    public void addMachine(Machine machine) {
        if (machine == null) {
            return;
        }

        if (!machines.containsKey(machine.getId())) {
            machines.put(machine.getId(), machine);

            System.out.println("Maschine hinzugefügt: " + machine.getId() + "#" + machine.getName());

            /* listener informieren */
            listeners.forEach((listener) -> {listener.machineAdded(machine);});
        }


        /* alle tools der Maschine mit hinzufügen */
        machine.getTools().forEach((this::addTool));
    }

    /**
     * Fügt ein Werkzeug hinzu.
     * @param tool Das Werkzeug.
     */
    public void addTool(Tool tool) {
        if (tool == null) {
            return;
        }

        if (!tools.containsKey(tool.getId())) {
            tools.put(tool.getId(), tool);

            System.out.println("Werkzeug hinzugefügt: " + tool.getId() + "#" + tool.getName());

            /* listener informieren */
            listeners.forEach((listener) -> {listener.toolAdded(tool);});
        }
    }

    /**
     * Entfernt eine Maschine.
     * @param machine Die zu entfernende Maschine.
     */
    public void removeMachine(Machine machine) {
        if (machine == null) {
            return;
        }

        if (machines.containsKey(machine.getId())) {
            machines.remove(machine.getId());

            System.out.println("Maschine entfernt: " + machine.getId() + "#" + machine.getName());

            /* listener informieren */
            listeners.forEach((listener) -> {listener.machineRemoved(machine);});
        }

    }

    /**
     * Entfernt ein Werkzeug.
     * @param tool Das zu entfernende Werkzeug.
     */
    public void removeTool(Tool tool) {
        if (tool == null) {
            return;
        }

        if (tools.containsKey(tool.getId())) {
            tools.remove(tool.getId());

            System.out.println("Werkzeug entfernt: " + tool.getId() + "#" + tool.getName());

            /* listener informieren */
            listeners.forEach((listener) -> {listener.toolRemoved(tool);});
        }

    }

    /**
     * Gibt eine Maschine mit einem bestimmten Namen aus.
     * @param name Name der Maschine
     * @return Maschine mit dem Namen (die erste, die es findet).
     */
    public Machine getMachineByName(String name) {
        for (Machine machine : getFilteredMachines()) {
            if (machine.getName().equals(name))
                return machine;
        }
        return null;
    }

    /**
     * Gibt ein Werkzeug mit einem bestimmten Namen aus.
     * @param name Der Name des Werkzeugs.
     * @return Werkzeug mit dem Namen (das erste, das es findet).
     */
    public Tool getToolByName(String name) {
        for (Tool tool : getFilteredTools()) {
            if (tool.getName().equals(name))
                return tool;
        }
        return null;
    }

    public void addListener(FactoryChangeListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(FactoryChangeListener listener) {
        while (listeners.contains(listener))
            listeners.remove(listener);
    }

    public Collection<Machine> getFilteredMachines() {
        if (filteredMachines != null)
            return filteredMachines;

        return getAllMachines();
    }

    public Collection<Machine> getAllMachines() {
        return Collections.unmodifiableCollection(machines.values());
    }

    public Collection<Tool> getFilteredTools() {
        if (filteredTools != null)
            return filteredTools;

        return getAllTools();
    }

    public Collection<Tool> getAllTools() {
        return Collections.unmodifiableCollection(tools.values());
    }

    public Period getFilterPeriod() {
        if (filterPeriod != null)
            return filterPeriod;

        if (xmlPeriod == null)
            return null;

        Date start = xmlPeriod.getStart();
        Date end = xmlPeriod.getEnd();

        StartFilter startFilter = StaffEntryFilter.getActive().getFirstFilterOfType(StartFilter.class);
        EndFilter endFilter = StaffEntryFilter.getActive().getFirstFilterOfType(EndFilter.class);

        if (startFilter != null && startFilter.getStart() != null)
            start = startFilter.getStart();

        if (endFilter != null && endFilter.getEnd() != null)
            end = endFilter.getEnd();

        if (end.before(start))
            end = start;

        filterPeriod = new Period(start, end);

        return filterPeriod;
    }

    public Period getPeriod() {
        return xmlPeriod;
    }

    public Machine getMachineById(int id) {
        if (machines.containsKey(id))
            return machines.get(id);

        return null;
    }

    public Tool getToolById(int id) {
        if (tools.containsKey(id))
            return tools.get(id);

        return null;
    }

    @Override
    public Wochenplan clone() {
        Wochenplan clone = new Wochenplan();
        clone.filterPeriod = filterPeriod;
        clone.machines = machines;
        clone.tools = tools;
        clone.listeners = listeners;

        return clone;
    }
}
