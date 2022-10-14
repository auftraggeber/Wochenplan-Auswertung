package me.langner.jonas.wpapp.objects;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.listener.FactoryChangeListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WochenplanTest {

    private static Wochenplan wochenplan;
    private static FactoryChangeListener listener;
    private static int machineAddedCount = 0, machineRemovedCount = 0, toolAddedCount = 0, toolRemovedCount = 0;

    @BeforeAll
    static void init() {
        wochenplan = new Wochenplan();
        listener = new FactoryChangeListener() {
            @Override
            public void machineAdded(Machine machine) {
                ++machineAddedCount;
            }

            @Override
            public void toolAdded(Tool tool) {
                ++toolAddedCount;
            }

            @Override
            public void machineRemoved(Machine machine) {
                ++machineRemovedCount;
            }

            @Override
            public void toolRemoved(Tool tool) {
                ++toolRemovedCount;
            }
        };
        wochenplan.addListener(
                listener
        );
    }

    @BeforeEach
    void setUp() {



    }

    @Test
    void addMachine() {
        Machine machine = new Machine(1, "Testmaschine");
        final int c = machineAddedCount;

        assertTrue(wochenplan.getMachines().isEmpty());
        wochenplan.addMachine(machine);
        assertEquals(1, wochenplan.getMachines().size());
        assertEquals(machine, wochenplan.getMachines().stream().findFirst().get());
        assertNull(wochenplan.getMachineById(0));
        assertNull(wochenplan.getMachineByName("TESTMASCHINE"));
        assertEquals(machine, wochenplan.getMachineById(1));
        assertEquals(machine, wochenplan.getMachineByName("Testmaschine"));
        assertEquals(c+1, machineAddedCount);
    }

    @Test
    void addTool() {
        Tool tool = new Tool(1, "Testtool", 120);
        final int c = toolAddedCount;

        assertTrue(wochenplan.getTools().isEmpty());
        wochenplan.addTool(tool);
        assertEquals(1, wochenplan.getTools().size());
        assertEquals(tool, wochenplan.getTools().stream().findFirst().get());
        assertNull(wochenplan.getToolById(0));
        assertNull(wochenplan.getToolByName("TESTTOOL"));
        assertEquals(tool, wochenplan.getToolById(1));
        assertEquals(tool, wochenplan.getToolByName("Testtool"));
        assertEquals(c+1, toolAddedCount);
    }

    @Test
    void removeMachine() {
        Machine machine = new Machine(1, "Testmaschine");
        Machine notAddedMachine = new Machine(2, "Testmaschine2");
        final int c = machineRemovedCount;

        assertEquals(1, wochenplan.getMachines().size());
        wochenplan.removeMachine(notAddedMachine);
        assertEquals(1, wochenplan.getMachines().size());
        wochenplan.removeMachine(machine);
        assertEquals(0, wochenplan.getMachines().size());
        assertEquals(c+1, machineRemovedCount);
    }

    @Test
    void removeTool() {
        Tool tool = new Tool(1, "Testtool", 120);
        Tool notAddedTool = new Tool(2, "Testtool2", 180);
        final int c = toolRemovedCount;

        assertEquals(1, wochenplan.getTools().size());
        wochenplan.removeTool(notAddedTool);
        assertEquals(1, wochenplan.getTools().size());
        wochenplan.removeTool(tool);
        assertEquals(0, wochenplan.getTools().size());
        assertEquals(c+1, toolRemovedCount);
    }


    @Test
    void addNullMachine() {
        final int c = machineAddedCount;
        assertDoesNotThrow(() -> wochenplan.addMachine(null));
        assertEquals(c, machineAddedCount);
    }

    @Test
    void addNullTool() {
        final int c = toolAddedCount;
        assertDoesNotThrow(() -> wochenplan.addTool(null));
        assertEquals(c, toolAddedCount);
    }

    @Test
    void removeNullMachine() {
        final int c = machineRemovedCount;
        assertDoesNotThrow(() -> wochenplan.removeMachine(null));
        assertEquals(c, machineRemovedCount);
    }

    @Test
    void removeNullTool() {
        final int c = toolRemovedCount;
        assertDoesNotThrow(() -> wochenplan.removeTool(null));
        assertEquals(c, toolRemovedCount);
    }
}