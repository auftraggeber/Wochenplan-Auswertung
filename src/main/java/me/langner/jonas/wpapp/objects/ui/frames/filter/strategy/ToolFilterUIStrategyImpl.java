package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.filter.ToolFilter;

import java.util.ArrayList;

public class ToolFilterUIStrategyImpl extends BasicFactoryElementFilterUIStrategyImpl<Tool> implements IFactoryElementFilterUIStrategy {

    private ToolFilter toolFilter = null;

    public ToolFilterUIStrategyImpl() {
        super(WPAPP.getWochenplan().getTools(), new ArrayList<>());
    }

    public ToolFilterUIStrategyImpl(ToolFilter toolFilter) {
        this();
        this.toolFilter = toolFilter;
        addElements(toolFilter.getTools());
    }

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
