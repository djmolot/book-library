package company.name.library.exceptions;

public class DaoLayerException extends RuntimeException {
    public DaoLayerException(String message) {
        super(message);
    }

    public DaoLayerException(Throwable cause) {
        super(cause);
    }
}
