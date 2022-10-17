package me.langner.jonas.wpapp.objects.ui.frames.filter.strategy;

import me.langner.jonas.wpapp.objects.factory.FactoryElement;

import java.util.List;
import java.util.Vector;

/**
 * Definiert alle notwendigen Methoden, die das {@link me.langner.jonas.wpapp.objects.ui.frames.filter.FactoryElementFilterUI} benötigt,
 * um die Daten korrekt zu visualisieren.
 * @author Jonas Langner
 * @version 0.1.0
 * @since 17.10.22
 */
public interface IFactoryElementFilterUIStrategy {

    /**
     * Speichert die im UI angegebenen Daten ab.
     * Generiert dabei entweder ein neues Filterobjekt oder
     * verändert ein schon bestehendes.
     */
    void save();

    /**
     * Verknüpft die Eigenschaft mit dem Filter.
     * D.h. der Filter lässt diese Eigenschaft passieren.
     * @param element Das Element, welches die Eigenschaft repräsentiert.
     * @return Gibt an, ob die Eigenschaft im Filter hinterlegt werden konnte.
     */
    boolean addElement(FactoryElement element);

    /**
     * Verknüpft mehrere Eigenschaften mit dem Filter.
     * @param elements Die Eigenschaften, die zum Filter hinzugefügt werden sollen.
     * @return Gibt an, ob mindestens eine Eigenschaft zum Filter hinzugefügt werden konnte.
     */
    boolean addElements(List<? extends FactoryElement> elements);

    /**
     * Entfernt die Verknüpfung zwischen einer Eigenschaft und dem Filter.
     * @param element Die Eigenschaft, die nicht mehr mit dem Filter in Verbindung stehen soll.
     * @return Gibt an, ob die Eigenschaft vom Filter entfernt werden konnte.
     */
    boolean removeElement(FactoryElement element);

    /**
     * Entfernt die Verknüpfung mehrerer Eigenschaften mit dem Filter.
     * @param elements Die Eigenschaften, die nicht mehr mit dem Filter in Verbindung stehen sollen.
     * @return Gibt an, ob mindestens eine Eigenschaft vom Filter entfernt werden konnte.
     */
    boolean removeElements(List<? extends FactoryElement> elements);

    /**
     * Ermittelt die Eigenschaften, die zum Filter hinzugefügt werden können.
     * @return Alle Eigenschaften, die noch zum Filter hinzugefügt werden können.
     */
    Vector<? extends FactoryElement> getElementsToAdd();

    /**
     * Ermittelt die Eigenschaften, die schon zum Filter hinzugefügt werden konnten.
     * @return Alle Eigenschaften, die zum Filter hinzugefügt wurden.
     */
    Vector<? extends FactoryElement> getAddedElements();

    /**
     * Ermittelt den Titel des UI-Fensters.
     * @return Der Titel, des Fensters.
     */
    String getTitle();
}
