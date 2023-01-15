package company.name.library.exceptions;

public class ServiceLayerException extends RuntimeException {
    public ServiceLayerException(String message) {
        super(message);
    }

    public ServiceLayerException(Throwable cause) {
        super(cause);
    }
}
