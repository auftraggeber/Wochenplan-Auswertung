package me.langner.jonas.wpapp.listener;

import me.langner.jonas.wpapp.objects.Machine;
import me.langner.jonas.wpapp.objects.Tool;

/**
 * Schnittstelle, die bei Änderungen aufgerufen wird.
 */
public interface FactoryChangeListener {

    void machineAdded(Machine machine);
    void toolAdded(Tool tool);
    void machineRemoved(Machine machine);
    void toolRemoved(Tool tool);

}
