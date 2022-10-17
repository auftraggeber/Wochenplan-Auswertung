package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.filter.MachineFilter;

import java.util.ArrayList;

/**
 * Die restliche Implementation, um im {@link me.langner.jonas.wpapp.objects.ui.frames.filter.FactoryElementFilterUI} die
 * {@link Machine}n-Informationen anzuzeigen.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public class MachineFilterUIStrategyImpl extends BasicFactoryElementFilterUIStrategyImpl<Machine> implements IFactoryElementFilterUIStrategy {

    private MachineFilter machineFilter = null;

    /**
     * Setzt die Standarddaten für die Liste so, dass alle bekannten Maschinen in der Liste
     * "Noch hinzufügbar" stehen.
     */
    public MachineFilterUIStrategyImpl() {
        super(WPAPP.getWochenplan().getAllMachines(), new ArrayList<>());
    }

    /**
     * Übergibt einen Filter und setzt sie Listen des UIs so, dass sie den Filter
     * repräsentieren.
     * @param machineFilter Der Filter, der in das UI geladen werden soll.
     */
    public MachineFilterUIStrategyImpl(MachineFilter machineFilter) {
        this();
        this.machineFilter = machineFilter;
        addElements(machineFilter.getMachines());
    }

    /**
     * Speichert die im UI angegebenen Daten ab.
     * Generiert dabei entweder ein neues Filterobjekt oder
     * verändert ein schon bestehendes.
     */
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
