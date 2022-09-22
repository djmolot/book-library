package company.name.exceptions;

public class NoEntityWithSuchIdException extends Exception {
    public NoEntityWithSuchIdException(String message) {
        super(message);
    }
}
