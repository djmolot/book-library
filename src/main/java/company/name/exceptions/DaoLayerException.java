package company.name.exceptions;

public class DaoLayerException extends RuntimeException {
    public DaoLayerException(String message) {
        super(message);
    }

    public DaoLayerException(Throwable cause) {
        super(cause);
    }
}
