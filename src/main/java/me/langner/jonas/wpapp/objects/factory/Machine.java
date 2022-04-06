package me.langner.jonas.wpapp.objects.factory;

import java.util.*;

/**
 * Eine Maschine, auf der die Werkzeuge zur Produktion liegen
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class Machine extends FactoryElement {

    private List<Tool> tools = new ArrayList<>();

    /**
     * Erstellt eine neue Maschine.
     * @param id Die ID der Maschine.
     * @param name Der Name der Maschine
     */
    public Machine(int id, String name) {
        super(id, name);
    }

    /**
     * Fügt ein Werkzeug zur Maschine hinzu.
     * @param tool Das hinzuzufügende Werkzeug.
     */
    public void addTool(Tool tool) {
        if (!tools.contains(tool))
            tools.add(tool);
    }

    /**
     * Entfernt ein Werkzeug von der Maschine.
     * @param tool Das zu entfernende Werkzeug.
     */
    public void removeTool(Tool tool) {
        while (tools.contains(tool))
            tools.remove(tool);
    }

    public List<Tool> getTools() {
        return Collections.unmodifiableList(tools);
    }
}
