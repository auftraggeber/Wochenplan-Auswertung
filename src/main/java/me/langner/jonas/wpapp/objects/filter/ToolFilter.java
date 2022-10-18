package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.factory.Tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public ToolFilter(List<Tool> toolList) {
        super();
        this.toolList.addAll(toolList);
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return toolList.contains(entry.getTool());
    }

    public void setTools(List<Tool> toolList) {
        this.toolList = toolList;
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
}
