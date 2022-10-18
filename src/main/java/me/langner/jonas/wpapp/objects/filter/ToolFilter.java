package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.factory.Tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Ein Filter der {@link StaffEntry}s nach bestimmten {@link Tool}s filtert.
 * Nur die Tools, die im Filter hinterlegt sind, werden vom Filter auch zugelassen.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public class ToolFilter extends StaffEntryFilter {

    private static final long serialVersionUID = 1L;

    private List<Tool> toolList = new ArrayList<>();
    private List<Tool> filteredTools = null;

    private Vector<StaffEntryFilter> lastStack = null;

    public ToolFilter(List<Tool> toolList) {
        super();
        this.toolList.addAll(toolList);
        WPAPP.getWochenplan().resetFilteredTools();
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return toolList.contains(entry.getTool());
    }

    public void setTools(List<Tool> toolList) {
        if (!this.toolList.equals(toolList))
            lastStack = null;

        this.toolList = toolList;
        WPAPP.getWochenplan().resetFilteredTools();
    }

    public List<Tool> getTools() {
        return Collections.unmodifiableList(toolList);
    }

    @Override
    public String getLongDescription() {
        if (toolList.isEmpty()) {
            return "Kein Werkzeug";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < toolList.size(); i++) {
            builder.append("\n- ");
            builder.append(toolList.get(i).getName());
        }

        return "Werkzeuge: " + builder.toString();
    }

    @Override
    public String getShortDescription() {
        if (toolList.isEmpty()) {
            return "Kein Werkzeug";
        }
        if (toolList.size() <= 1) {
            return "Ein Werkzeug";
        }

        return toolList.size() + " Werkzeuge";
    }

    public List<Tool> getFilteredTools() {
        if (lastStack != null && lastStack.equals(getFilterStack()))
            return filteredTools;

        ToolFilter filter = getNextFilterOfType(ToolFilter.class);

        filteredTools = new ArrayList<>();
        lastStack = getFilterStack();

        if (filter != null) {
            for (Tool tool : filter.getFilteredTools()) {
                if (toolList.contains(tool))
                    filteredTools.add(tool);
            }
        } else filteredTools = getTools();

        return filteredTools;
    }
}
