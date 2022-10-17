package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.filter.MachineFilter;

import java.util.ArrayList;
import java.util.Collection;

public class MachineFilterUIStrategyImpl extends BasicFactoryElementFilterUIStrategyImpl<Machine> implements IFactoryElementFilterUIStrategy {

    private MachineFilter machineFilter = null;

    public MachineFilterUIStrategyImpl() {
        super(WPAPP.getWochenplan().getMachines(), new ArrayList<>());
    }

    public MachineFilterUIStrategyImpl(MachineFilter machineFilter) {
        this();
        this.machineFilter = machineFilter;
        addElements(machineFilter.getMachines());
    }

    @Override
    public void save() {
        if (machineFilter == null)
            machineFilter = new MachineFilter(getAdded());
        else
            machineFilter.setMachines(getAdded());
    }

    @Override
    public String getTitle() {
        return "Maschinen filtern";
    }
}
