package me.langner.jonas.wpapp.objects;

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

    private Period period, xmlPeriod;
    private Map<Integer, Machine> machines = new HashMap<>();
    private Map<Integer, Tool> tools = new HashMap<>();
    private Set<FactoryChangeListener> listeners = new HashSet<>();

    public void setPeriod(Period period) {
        this.period = period;

        System.out.println("Zeitraum gesetzt: " + period.getStartDisplay() + " bis " + period.getEndDisplay());
    }

    public void setXMLPeriod(Period period) {
        this.xmlPeriod = period;
        setPeriod(period);
    }

    /**
     * Fügt eine Maschine hinzu. Alle Werkzeuge der Maschine werden ebenso hinzugefügt.
     * @param machine Die Maschine
     */
    public void addMachine(Machine machine) {
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
        for (Machine machine : getMachines()) {
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
        for (Tool tool : getTools()) {
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

    public Collection<Machine> getMachines() {
        return Collections.unmodifiableCollection(machines.values());
    }

    public Collection<Tool> getTools() {
        return Collections.unmodifiableCollection(tools.values());
    }

    public Period getPeriod() {
        return period;
    }

    public Period getXMLPeriod() {
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
        clone.period = period;
        clone.machines = machines;
        clone.tools = tools;
        clone.listeners = listeners;

        return clone;
    }
}
