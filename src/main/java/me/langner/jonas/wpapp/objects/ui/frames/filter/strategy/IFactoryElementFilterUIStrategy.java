package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.objects.factory.FactoryElement;

import java.util.List;
import java.util.Vector;

public interface IFactoryElementFilterUIStrategy {

    void save();

    boolean addElement(FactoryElement element);

    boolean addElements(List<? extends FactoryElement> elements);

    boolean removeElement(FactoryElement element);

    boolean removeElements(List<? extends FactoryElement> elements);

    Vector<? extends FactoryElement> getElementsToAdd();

    Vector<? extends FactoryElement> getAddedElements();

    String getTitle();
}
