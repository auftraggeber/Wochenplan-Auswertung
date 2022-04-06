package me.langner.jonas.wpapp.objects.factory;

import me.langner.jonas.wpapp.objects.factory.FactoryElement;

/**
 * Ein Werkzeug, welche benötigt wird, um bestimmte Dinge herzustellen.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0
 */
public class Tool extends FactoryElement {

    private float preparationTime;

    /**
     * Erstellt ein neues Werkzeug.
     * @param id Die ID des Werkzeugs.
     * @param name Der Name des Werkzeugs.
     * @param preparationTime Die Rüstzeit des Werkzeugs.
     */
    public Tool(int id, String name, float preparationTime) {
        super(id, name);
        this.preparationTime = preparationTime;
    }

    public float getPreparationTime() {
        return preparationTime;
    }
}
