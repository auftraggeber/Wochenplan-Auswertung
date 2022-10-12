package me.langner.jonas.wpapp.objects;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import me.langner.jonas.wpapp.objects.time.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class StaffEntryTest {

    private StaffEntry staffEntry;
    private Machine machine;
    private Tool tool;
    private final Date now = new Date();

    @BeforeEach
    void setUp() {
        WPAPP.getWochenplan().setPeriod(new Period(now, now));
        machine = new Machine(1, "Testmaschine");
        tool = new Tool(1, "Testwerkzeug", 90);
        WPAPP.getWochenplan().addMachine(machine);
        WPAPP.getWochenplan().addTool(tool);
        staffEntry = new StaffEntry(machine, tool, now, 0, 1.5F, false);
    }

    @Test
    void testIfAddedToFactoryElements() {
        assertTrue(machine.getEntries().contains(staffEntry));
        assertTrue(tool.getEntries().contains(staffEntry));
    }

    @Test
    void hasPreparation() {
        StaffEntry secondStaffEntry = new StaffEntry(new Machine(1, "Testmaschine"), new Tool(1, "Testwerkzeug", 90), now, 1, 1.5F, true);

        assertFalse(staffEntry.hasPreparation());
        assertTrue(secondStaffEntry.hasPreparation());
    }

    @Test
    void testCreateWithNullData() {
        assertThrows(IllegalArgumentException.class, () -> new StaffEntry(null, tool, now, 0, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new StaffEntry(machine, null, now, 0, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new StaffEntry(machine, tool, null, 0, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new StaffEntry(machine, tool, now, -1, 0, false));
    }

    @Test
    void merge() {
        StaffEntry s1 = new StaffEntry(machine, tool, now, 0, 1.5F, false);
        StaffEntry s2 = new StaffEntry(machine, tool, now, 0, 0F, true);

        assertEquals(s1, s1.merge(s2));
        assertEquals(1.5F, s1.getValue());
        assertTrue(s1.hasPreparation());

        s1 = new StaffEntry(machine, tool, now, 2, 1.5F, false);
        s2 = new StaffEntry(machine, tool, now, 2, 0F, true);

        assertEquals(s2, s2.merge(s1));
        assertEquals(1.5F, s2.getValue());
        assertTrue(s2.hasPreparation());
    }

    @Test
    void testMergeFailures() {
        Date tomorrow = new Date(now.getTime() + 86400000);
        final StaffEntry s1 = new StaffEntry(machine, tool, tomorrow, 0, 1.5F, false);
        final StaffEntry s2 = new StaffEntry(machine, tool, tomorrow, 0, 0F, false);

        assertThrows(IllegalArgumentException.class, () -> s1.merge(s2));
        assertThrows(IllegalArgumentException.class, () -> s2.merge(s1));

        final StaffEntry s3 = new StaffEntry(machine, tool, tomorrow, 0, 1.5F, true);
        final StaffEntry s4 = new StaffEntry(machine, tool, tomorrow, 0, 0F, true);

        assertThrows(IllegalArgumentException.class, () -> s3.merge(s4));
        assertThrows(IllegalArgumentException.class, () -> s4.merge(s3));

        final StaffEntry s5 = new StaffEntry(machine, tool, tomorrow, 0, 1.5F, false);
        final StaffEntry s6 = new StaffEntry(machine, tool, now, 0, 0F, true);

        assertThrows(IllegalArgumentException.class, () -> s5.merge(s6));
        assertThrows(IllegalArgumentException.class, () -> s6.merge(s5));

        final StaffEntry s7 = new StaffEntry(machine, tool, tomorrow, 0, 1.5F, true);
        final StaffEntry s8 = new StaffEntry(new Machine(2, "Testmaschine2"), tool, tomorrow, 0, 0F, false);

        assertThrows(IllegalArgumentException.class, () -> s7.merge(s8));
        assertThrows(IllegalArgumentException.class, () -> s8.merge(s7));
    }

    @Test
    void testEquals() {
        Date plusTwoDays = new Date(now.getTime() + 172800000);
        StaffEntry s1 = new StaffEntry(machine, tool, plusTwoDays, 0, 1.5F, false);
        StaffEntry s2 = new StaffEntry(machine, tool, plusTwoDays, 0, 1.5F, false);
        StaffEntry s3 = new StaffEntry(machine, tool, plusTwoDays, 0, 1.5F, true);

        assertEquals(s1, s2);
        assertEquals(s1, s3);
    }
}