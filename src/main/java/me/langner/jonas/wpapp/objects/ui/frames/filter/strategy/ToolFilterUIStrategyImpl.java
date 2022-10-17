package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.filter.ToolFilter;

import java.util.ArrayList;

/**
 * Die restliche Implementation, um im {@link me.langner.jonas.wpapp.objects.ui.frames.filter.FactoryElementFilterUI} die
 * {@link Tool}s-Informationen anzuzeigen.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public class ToolFilterUIStrategyImpl extends BasicFactoryElementFilterUIStrategyImpl<Tool> implements IFactoryElementFilterUIStrategy {

    private ToolFilter toolFilter = null;

    /**
     * Setzt die Standarddaten für die Liste so, dass alle bekannten Werkzeuge in der Liste
     * "Noch hinzufügbar" stehen.
     */
    public ToolFilterUIStrategyImpl() {
        super(WPAPP.getWochenplan().getTools(), new ArrayList<>());
    }

    /**
     * Übergibt einen Filter und setzt sie Listen des UIs so, dass sie den Filter
     * repräsentieren.
     * @param toolFilter Der Filter, der in das UI geladen werden soll.
     */
    public ToolFilterUIStrategyImpl(ToolFilter toolFilter) {
        this();
        this.toolFilter = toolFilter;
        addElements(toolFilter.getTools());
    }

    /**
     * Speichert die im UI angegebenen Daten ab.
     * Generiert dabei entweder ein neues Filterobjekt oder
     * verändert ein schon bestehendes.
     */
    @Override
    public void save() {
        if (toolFilter == null)
            toolFilter = new ToolFilter(getAdded());
        else
            toolFilter.setTools(getAdded());
    }

    @Override
    public String getTitle() {
        return "Werkzeuge filtern";
    }
}
