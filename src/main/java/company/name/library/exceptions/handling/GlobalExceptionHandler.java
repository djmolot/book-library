package company.name.library.exceptions.handling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import company.name.library.entities.ApiError;
import company.name.library.entities.ErrorResponse;
import company.name.library.exceptions.DaoLayerException;
import company.name.library.exceptions.ServiceLayerException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
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
        return composeResponse(ex, "Unknown error was detected. Please contact the support service.",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceLayerException.class)
    public final ResponseEntity<Object> handleServiceLayerException(ServiceLayerException ex) {
        return composeResponse(ex, "Service layer Error", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> composeResponse(Exception ex, String errorMessage, HttpStatus status) {
        ErrorResponse errorResponse;
        String localDT = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        if (ex.getClass() == DaoLayerException.class) {
            errorResponse = new ErrorResponse(localDT, errorMessage);
        } else if (ex instanceof BindException bindEx) {
            List<ApiError> errors = bindEx.getBindingResult().getFieldErrors().stream()
                    .map(fieldError -> new ApiError(fieldError.getField(),
                            fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());
            errorResponse = new ErrorResponse(localDT, errorMessage, errors);
        } else {
            errorMessage += ". " + ex.getMessage();
            errorResponse = new ErrorResponse(localDT, errorMessage);
        }
        return ResponseEntity.status(status).body(errorResponse);
    }

}