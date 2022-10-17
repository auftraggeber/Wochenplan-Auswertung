package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.factory.Machine;

import java.util.List;

/**
 * Ein Filter, welcher nach bestimmten {@link Machine}n filtert.
 * Nur {@link StaffEntry}s, die auf einer Maschine, die auch im Filter angegeben ist, hinterlegt sind,
 * werden vom filter akzeptiert.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public class MachineFilter extends StaffEntryFilter {

    private List<Machine> machineList;

    public MachineFilter(List<Machine> machineList) {
        super();
        this.machineList = machineList;
    }

    @Override
    public boolean staffEntryGetsAccepted(StaffEntry entry) {
        return machineList.contains(entry.getMachine());
    }

    public List<Machine> getMachines() {
        return machineList;
    }
}
