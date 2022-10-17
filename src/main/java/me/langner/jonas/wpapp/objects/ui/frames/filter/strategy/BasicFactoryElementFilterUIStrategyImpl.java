package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.WPAPP;
import me.langner.jonas.wpapp.objects.factory.FactoryElement;
import me.langner.jonas.wpapp.objects.factory.Machine;
import me.langner.jonas.wpapp.objects.filter.MachineFilter;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public abstract class BasicFactoryElementFilterUIStrategyImpl<T extends FactoryElement> implements IFactoryElementFilterUIStrategy {

    private final Vector<T> toAdd = new Vector<>();
    private final Vector<T> added = new Vector<>();

    public BasicFactoryElementFilterUIStrategyImpl(Collection<T> toAdd, Collection<T> added) {
        this.toAdd.addAll(toAdd);
        this.added.addAll(added);
    }

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

    @Override
    public boolean addElements(List<? extends FactoryElement> elements) {
        boolean hasChanged = false;

        for (FactoryElement element : elements) {
            if (addElement(element))
                hasChanged = true;
        }

        return hasChanged;
    }

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

    @Override
    public boolean removeElements(List<? extends FactoryElement> elements) {
        boolean hasChanged = false;

        for (FactoryElement element : elements) {
            if (removeElement(element))
                hasChanged = true;
        }

        return hasChanged;
    }

    @Override
    public Vector<? extends FactoryElement> getElementsToAdd() {
        return toAdd;
    }

    @Override
    public Vector<? extends FactoryElement> getAddedElements() {
        return added;
    }

    protected Vector<T> getToAdd() {
        return toAdd;
    }

    protected Vector<T> getAdded() {
        return added;
    }

}
