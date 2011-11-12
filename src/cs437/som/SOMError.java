package cs437.som;

/**
 * Generic error class for self-organizing maps.
 */
public class SOMError extends Error {
    private static final long serialVersionUID = 0L;

    public SOMError(String message) {
        super(message);
    }

}
