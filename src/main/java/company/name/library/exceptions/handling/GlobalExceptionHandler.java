package company.name.library.exceptions.handling;

import java.util.List;
import java.util.Objects;

import company.name.library.entities.ErrorResponse;
import company.name.library.exceptions.DaoLayerException;
import company.name.library.exceptions.ServiceLayerException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        return composeResponse(ex, "Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValidEx(MethodArgumentNotValidException ex) {
        return composeResponse(ex, "ArgumentValidation Error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationEx(ConstraintViolationException ex) {
        return composeResponse(ex, "ConstraintViolation Error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DaoLayerException.class)
    public final ResponseEntity<Object> handleDaoLayerException(DaoLayerException ex) {
        return composeResponse(ex, "DAO layer Error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceLayerException.class)
    public final ResponseEntity<Object> handleServiceLayerException(ServiceLayerException ex) {
        return composeResponse(ex, "Service layer Error", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> composeResponse(Exception ex, String errorMessage, HttpStatus status) {
        List<String> details;
        if (ex instanceof BindException bindEx) {
            details = bindEx.getBindingResult().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .toList();
        } else {
            details = List.of(ex.getLocalizedMessage());
        }
        ErrorResponse error = new ErrorResponse(errorMessage, details);
        return ResponseEntity.status(status).body(error);
    }

}