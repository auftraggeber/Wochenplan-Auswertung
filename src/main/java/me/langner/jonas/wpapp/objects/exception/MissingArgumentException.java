package me.langner.jonas.wpapp.objects.exception;

/**
 * Wird aufgerufen, wenn ein Parser zu wenig Argumente hat, um ein Objekt zu erstellen.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public class MissingArgumentException extends RuntimeException {

    private Object[][] arguments;

    /**
     * Erstellt eine neue Exception.
     * @param message Die Fehlermeldung.
     */
    public MissingArgumentException(String message, Object[][] arguments) {
        super(message);

        this.arguments = arguments;
    }

    public String getArgumentString() {

        String args = "";

        for (Object[] object : arguments) {

            if (object.length >= 2) {

                if (args != "")
                    args += "; ";

                args += object[0] + "= " + object[1];
            }
        }

        return args;
    }
}
