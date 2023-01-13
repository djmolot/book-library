package company.name.library.controllers;

import java.util.List;

import company.name.library.exceptions.DaoLayerException;
import company.name.library.exceptions.ServiceLayerException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        List<String> details = List.of(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> details = List.of(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("ConstraintViolation Error", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DaoLayerException.class)
    public final ResponseEntity<Object> handleDaoLayerException(DaoLayerException ex) {
        List<String> details = List.of(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("DAO layer Error", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ServiceLayerException.class)
    public final ResponseEntity<Object> handleServiceLayerException(ServiceLayerException ex) {
        List<String> details = List.of(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Service layer Error", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}