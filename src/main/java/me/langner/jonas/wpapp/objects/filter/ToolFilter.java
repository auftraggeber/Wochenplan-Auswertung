package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.factory.Tool;

import java.util.List;

/**
 * Ein Filter der {@link StaffEntry}s nach bestimmten {@link Tool}s filtert.
 * Nur die Tools, die im Filter hinterlegt sind, werden vom Filter auch zugelassen.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public class ToolFilter extends StaffEntryFilter {

    private List<Tool> toolList;

    public ToolFilter(List<Tool> toolList) {
        super();
        this.toolList = toolList;
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return toolList.contains(entry.getTool());
    }

    public List<Tool> getTools() {
        return toolList;
    }
}
