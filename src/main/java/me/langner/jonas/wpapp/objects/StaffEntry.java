package me.langner.jonas.wpapp.objects;

import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.factory.Tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Ein Eintrag mit Personal.
 * @author Jonas Langner
 * @version 1.0.1
 * @since 1.0
 */
public class StaffEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int lastJavaID = 0;
    private int javaID;

    private Machine machine;
    private Tool tool;
    private Date date;
    private int shift;
    private float value;
    private boolean preparation;

    private int mergedWith = -1;

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
        if (machine == null || tool == null || date == null || shift < 0)
            throw new IllegalArgumentException("Key arguments must not be null or negative!");

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

    /**
     * Fügt zwei Einträge mit den gleichen Schlüsselattributen zusammen.
     * Dafür muss einer dieser Einträge eine Rüstung haben.
     * Der Eintrag mit der Rüstung wird priorisiert. D.h. der Eintrag mit der Rüstung gibt die Rüstung an den
     * zweiten Eintrag weiter. Der zweite Eintrg gibt dafür die Personalinformationen weiter.
     * <b>Achtung: Es wird nur ein Objekt angepasst.</b>
     * @param with Der Eintrag, dessen Werte mit diesem synchronisiert werden sollen.
     * @return Gibt das Objekt an, das angepasst wurde.
     * @throws IllegalArgumentException Falls die beiden Objekte nicht miteinander zusammengeführt werden können.
     */
    public StaffEntry merge(StaffEntry with) throws IllegalArgumentException {
        if (with != null) {
            if (mergedWith == with.getJavaID())
                return this;

            if (this.javaID == with.javaID)
                throw new IllegalArgumentException("Cannot merge with itself.");

            if (!this.date.equals(with.date) || this.shift != with.shift || !this.machine.equals(with.machine) || !this.tool.equals(with.tool))
                throw new IllegalArgumentException("Cannot merge due to different key attributes.");

            if (with.hasPreparation() && !hasPreparation()) {
                preparation = true;
                mergedWith = with.getJavaID();
            }
            else if (hasPreparation() && !with.hasPreparation()) {
                value = with.getValue();
                mergedWith = with.getJavaID();
            }
            else throw new IllegalArgumentException("Cannot merge two entries with preparation and value!");

            return this;
        }
        return null;
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

    @Override
    public String toString() {
        return "StaffEntry{" +
                "javaID=" + javaID +
                ", machine=" + machine +
                ", tool=" + tool +
                ", date=" + date +
                ", shift=" + shift +
                ", value=" + value +
                ", preparation=" + preparation +
                '}';
    }
}
