package me.langner.jonas.wpapp.objects.exception;

/**
 * Wird aufgerufen, wenn eine falsche Datei gewählt wurde.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class IllegalFileExtensionException extends RuntimeException {

    private String[] neededExceptions;

    /**
     * Erstellt eine neue Exception.
     * @param message Die Fehlermeldung.
     */
    public IllegalFileExtensionException(String message, String ... neededExceptions) {
        super(message);

        this.neededExceptions = neededExceptions;
    }

    public String[] getNeededExceptions() {
        return neededExceptions;
    }
}
