package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.FactoryElement;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.filter.MachineFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Eine Basisimplementation der Funktionen des {@link me.langner.jonas.wpapp.objects.ui.frames.filter.FactoryElementFilterUI}s,
 * die für die Listenverwaltung zuständig ist.
 * @param <T> Der Typ der Elemente, die in den Listen stehen.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public abstract class BasicFactoryElementFilterUIStrategyImpl<T extends FactoryElement> implements IFactoryElementFilterUIStrategy {

    private final Vector<T> toAdd = new Vector<>();
    private final Vector<T> added = new Vector<>();

    /**
     * Erstellt eine neue Instanz und übergibt die notwendigen Elemente mit deren Startposition.
     * @param toAdd Die Elemente, die in der Liste "Noch hinzufügbar" auftauchen sollen.
     * @param added Die Elemente, die in der Liste "Hinzugefügt" auftauchen sollen.
     */
    public BasicFactoryElementFilterUIStrategyImpl(Collection<T> toAdd, Collection<T> added) {
        this.toAdd.addAll(toAdd);
        this.added.addAll(added);
    }

    /**
     * Verknüpft die Eigenschaft mit dem Filter.
     * D.h. der Filter lässt diese Eigenschaft passieren.
     * @param element Das Element, welches die Eigenschaft repräsentiert.
     * @return Gibt an, ob die Eigenschaft im Filter hinterlegt werden konnte.
     */
    @Override
    public boolean addElement(FactoryElement element) {
        try {
            T t = (T) element;

            return toAdd.contains(t) && !added.contains(t) &&
                    toAdd.remove(t) && added.add(t);
        }
        catch (ClassCastException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    /**
     * Verknüpft mehrere Eigenschaften mit dem Filter.
     * @param elements Die Eigenschaften, die zum Filter hinzugefügt werden sollen.
     * @return Gibt an, ob mindestens eine Eigenschaft zum Filter hinzugefügt werden konnte.
     */
    @Override
    public boolean addElements(List<? extends FactoryElement> elements) {
        boolean hasChanged = false;

        for (FactoryElement element : elements) {
            if (addElement(element))
                hasChanged = true;
        }

        return hasChanged;
    }

    /**
     * Entfernt die Verknüpfung zwischen einer Eigenschaft und dem Filter.
     * @param element Die Eigenschaft, die nicht mehr mit dem Filter in Verbindung stehen soll.
     * @return Gibt an, ob die Eigenschaft vom Filter entfernt werden konnte.
     */
    @Override
    public boolean removeElement(FactoryElement element) {
        try {
            T t = (T) element;

            return !toAdd.contains(t) && added.contains(t) &&
                    added.remove(t) && toAdd.add(t);
        }
        catch (ClassCastException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    /**
     * Entfernt die Verknüpfung mehrerer Eigenschaften mit dem Filter.
     * @param elements Die Eigenschaften, die nicht mehr mit dem Filter in Verbindung stehen sollen.
     * @return Gibt an, ob mindestens eine Eigenschaft vom Filter entfernt werden konnte.
     */
    @Override
    public boolean removeElements(List<? extends FactoryElement> elements) {
        boolean hasChanged = false;

        for (FactoryElement element : elements) {
            if (removeElement(element))
                hasChanged = true;
        }

        return hasChanged;
    }

    /**
     * Ermittelt die Eigenschaften, die zum Filter hinzugefügt werden können.
     * @return Alle Eigenschaften, die noch zum Filter hinzugefügt werden können.
     */
    @Override
    public Vector<? extends FactoryElement> getElementsToAdd() {
        return toAdd;
    }

    /**
     * Ermittelt die Eigenschaften, die schon zum Filter hinzugefügt werden konnten.
     * @return Alle Eigenschaften, die zum Filter hinzugefügt wurden.
     */
    @Override
    public Vector<? extends FactoryElement> getAddedElements() {
        return added;
    }

    /**
     * @return Eine nicht abstrahierte Liste aller Elemente, die in der Liste "Noch hinzufügbar" stehen.
     */
    protected List<T> getToAdd() {
        return Collections.unmodifiableList(toAdd);
    }

    /**
     * @return Eine nicht abstrahierte Liste aller Elemente, die in der Liste "Hinzugefügt" stehen.
     */
    protected List<T> getAdded() {
        return Collections.unmodifiableList(added);
    }

}
