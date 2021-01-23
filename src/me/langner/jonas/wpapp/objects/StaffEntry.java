package me.langner.jonas.wpapp.objects;

import java.util.Date;
import java.util.Objects;

/**
 * Ein Eintrag mit Personal.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class StaffEntry {

    private static int lastJavaID = 0;
    private int javaID;

    private Machine machine;
    private Tool tool;
    private Date date;
    private int shift;
    private float value;
    private boolean preparation;

    /**
     * Erstellt einen neuen Eintrag
     * @param machine Die Maschine, die zum Eintrag gehört. Der Eintrag wird automatisch zur Maschine hinzugefügt.
     * @param tool Das Werkzeug, das zum Eintrag gehört. Der Eintrag wird automatisch zum Werkzeug hinzugefügt.
     * @param date Das Datum des Eintrags.
     * @param shift Die Schicht (0=FS, 1=SS, 2=NS)
     * @param value Der Wert des Eintrags.
     * @param preparation Gibt an, ob Rüstung oder nicht.
     */
    public StaffEntry(Machine machine, Tool tool, Date date, int shift, float value, boolean preparation) {
        this.machine = machine;
        this.tool = tool;
        this.date = date;
        this.shift = shift;
        this.value = value;
        this.preparation = preparation;

        this.javaID = ++lastJavaID;

        /* Daten an Maschine und Werkzeuge weitergeben */
        if (machine != null)
            machine.addEntry(this);
        if (tool != null)
            tool.addEntry(this);
    }
    public int getJavaID() {
        return javaID;
    }

    public Machine getMachine() {
        return machine;
    }

    public Tool getTool() {
        return tool;
    }

    public Date getDate() {
        return date;
    }

    public int getShift() {
        return shift;
    }

    public float getValue() {
        return value;
    }

    public boolean hasPreparation() {
        return preparation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffEntry that = (StaffEntry) o;
        return shift == that.shift &&
                value == that.value &&
                Objects.equals(machine, that.machine) &&
                Objects.equals(tool, that.tool) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machine, tool, date, shift, value);
    }
}
