package me.langner.jonas.wpapp.objects.listener;

import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;

/**
 * Schnittstelle, die bei Änderungen aufgerufen wird.
 */
public interface FactoryChangeListener {

    void machineAdded(Machine machine);
    void toolAdded(Tool tool);
    void machineRemoved(Machine machine);
    void toolRemoved(Tool tool);

}
