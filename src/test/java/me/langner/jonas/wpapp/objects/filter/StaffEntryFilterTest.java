package me.langner.jonas.wpapp.objects.filter;

import me.langner.jonas.wpapp.objects.StaffEntry;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StaffEntryFilterTest {

    private static List<StaffEntry> list;

    private static List<StaffEntry> getMockEntries() {
        if (list != null) {
            return list;
        }

        Machine machine1 = new Machine(999, "MockEntryMachine1");
        Machine machine2 = new Machine(1000, "MockEntryMachine2");
        Tool tool1 = new Tool(999, "MockEntryTool1", 90);
        Tool tool2 = new Tool(1000, "MockEntryTool2", 120);

        List<StaffEntry> entries = new ArrayList<>();

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + 3600 * 24 * 1000);
        Date yesterday = new Date(today.getTime() - 3600 * 24 * 1000);
        Date past = new Date(today.getTime() - 3600 * 24 * 1000 * 10);
        Date future = new Date(today.getTime() + 3600 * 24 * 1000 * 10);

        entries.add(new StaffEntry(machine1, tool1, today, 0, 1.5F, false));
        entries.add(new StaffEntry(machine1, tool1, tomorrow, 1, 0F, false));
        entries.add(new StaffEntry(machine1, tool2, yesterday, 0, 2F, true));
        entries.add(new StaffEntry(machine1, tool1, past, 0, 1.5F, false));
        entries.add(new StaffEntry(machine2, tool1, today, 2, 0F, true));
        entries.add(new StaffEntry(machine2, tool2, future, 1, 0F, false));

        list = Collections.unmodifiableList(entries);

        return list;
    }

    private StaffEntryFilter filter;

    @BeforeEach
    void setUp() {
        list = null;
        StaffEntryFilter.reset();
        filter = StaffEntryFilter.getActive();
    }

    @Test
    void testGetActive() {
        Date now = new Date();
        List<StaffEntry> entries = getMockEntries();
        assertEquals(filter, StaffEntryFilter.getActive());
        assertEquals(1, StaffEntryFilter.getActive().getFilterStackSize());
        assertEquals(6, StaffEntryFilter.getActive().filterStaffEntries(entries).size());


        StaffEntryFilter staffEntryFilter = new StartFilter(now);
        assertEquals(staffEntryFilter, StaffEntryFilter.getActive());
        assertEquals(2, StaffEntryFilter.getActive().getFilterStackSize());
        assertEquals(4, StaffEntryFilter.getActive().filterStaffEntries(entries).size());
    }

    @Test
    void testReset() {
        assertEquals(1, StaffEntryFilter.getActive().getFilterStackSize());
        assertEquals(filter, StaffEntryFilter.getActive());

        StaffEntryFilter filter = new StartFilter(new Date());
        assertEquals(filter, StaffEntryFilter.getActive());

        StaffEntryFilter.reset();
        assertNotEquals(filter, StaffEntryFilter.getActive());
        assertNotEquals(this.filter, StaffEntryFilter.getActive());

        List<StaffEntry> entries = getMockEntries();
        assertEquals(entries.size(), StaffEntryFilter.getActive().filterStaffEntries(entries).size());
    }

    @Test
    void filter() {
        new StartFilter(new Date());
        getMockEntries();
        new EndFilter(new Date());

        filter = StaffEntryFilter.getActive();

        assertTrue(filter.filter(list.get(0)));
        assertTrue(filter.filter(list.get(4)));
        assertFalse(filter.filter(list.get(1)));
        assertFalse(filter.filter(list.get(2)));
        assertFalse(filter.filter(list.get(3)));
        assertFalse(filter.filter(list.get(5)));

        assertEquals(2, filter.filterStaffEntries(list).size());
        assertTrue(filter.filterStaffEntries(list).contains(list.get(0)));
        assertTrue(filter.filterStaffEntries(list).contains(list.get(4)));

        StaffEntryFilter.reset();

        List<Machine> machineList = new ArrayList<>();
        machineList.add(list.get(5).getMachine());
        filter = new MachineFilter(machineList);

        assertEquals(2, filter.filterStaffEntries(list).size());
        assertEquals(list.get(4), filter.filterStaffEntries(list).get(0));
        assertEquals(list.get(5), filter.filterStaffEntries(list).get(1));

        List<Tool> toolList = new ArrayList<>();
        toolList.add(list.get(5).getTool());
        filter = new ToolFilter(toolList);
        assertEquals(1, filter.filterStaffEntries(list).size());
        assertEquals(list.get(5), filter.filterStaffEntries(list).get(0));
    }

    @Test
    void getFirstFilterOfType() {
        StartFilter first = new StartFilter(new Date());
        getMockEntries();
        new EndFilter(new Date());

        filter = StaffEntryFilter.getActive();

        assertEquals(3, filter.getFilterStackSize());

        assertNull(filter.getFirstFilterOfType(MachineFilter.class));
        assertEquals(first, filter.getFirstFilterOfType(StartFilter.class));
        assertEquals(2, filter.getFirstFilterOfType(StartFilter.class).getFilterStackSize());

        StartFilter second = new StartFilter(new Date(first.getStart().getTime() - 3600 * 24 * 1000));

        assertEquals(4, second.getFilterStackSize());

        filter = new MachineFilter(new ArrayList<>());

        assertEquals(5, filter.getFilterStackSize());
        assertEquals(second, filter.getFirstFilterOfType(StartFilter.class));
        assertEquals(3, filter.getFirstFilterOfType(EndFilter.class).getFilterStackSize());
        assertEquals(second, second.getFirstFilterOfType(StartFilter.class));
    }
}