package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.factory.Machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Ein Filter, welcher nach bestimmten {@link Machine}n filtert.
 * Nur {@link StaffEntry}s, die auf einer Maschine, die auch im Filter angegeben ist, hinterlegt sind,
 * werden vom filter akzeptiert.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public class MachineFilter extends StaffEntryFilter {

    private static final long serialVersionUID = 1L;

    private List<Machine> machineList = new ArrayList<>();
    private List<Machine> filteredMachines = null;

    private Vector<StaffEntryFilter> lastCheck = null;

    public MachineFilter(List<Machine> machineList) {
        super();
        this.machineList.addAll(machineList);
        WPAPP.getWochenplan().resetFilteredMachines();
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return machineList.contains(entry.getMachine());
    }

    public void setMachines(List<Machine> machineList) {
        if (!this.machineList.equals(machineList))
            lastCheck = null;
        this.machineList = machineList;
        WPAPP.getWochenplan().resetFilteredMachines();
    }

    public List<Machine> getMachines() {
        return Collections.unmodifiableList(machineList);
    }

    @Override
    public String getLongDescription() {
        if (machineList.isEmpty()) {
            return "Keine Maschine";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < machineList.size(); i++) {
            builder.append("\n- ");
            builder.append(machineList.get(i).getName());
        }

        return "Maschinen: " + builder.toString();
    }

    @Override
    public String getShortDescription() {
        if (machineList.isEmpty()) {
            return "Keine Maschine";
        }
        if (machineList.size() <= 1) {
            return "Eine Maschine";
        }

        return machineList.size() + " Maschinen";
    }

    /**
     * Geht durch den gesamten {@link #getFilterStack()} und überprüft, welche Maschinen nicht herausgefiltert werden.
     * @return Liste aller Maschinen, die den Filterstack passieren.
     */
    public List<Machine> getFilteredMachines() {
        if (lastCheck != null && lastCheck.equals(getFilterStack()))
            return filteredMachines;

        StaffEntryFilter next = getNextFilterOfType(MachineFilter.class);

        filteredMachines = new ArrayList<>();
        lastCheck = getFilterStack();

        if (next != null) {
            for (Machine machine : ((MachineFilter) next).getFilteredMachines()) {
                if (machineList.contains(machine))
                    filteredMachines.add(machine);
            }
        } else filteredMachines = getMachines();

        return filteredMachines;
    }
}
