package com.hs.healeon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    // CATCHES VALIDATION ERRORS {forms error -> returns obj}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // MODERN WAY USING STREAMS
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                fieldError -> fieldError.getField(), //getting the field name as key
                                fieldError -> fieldError.getDefaultMessage() != null //getting the error message as value
                                        ? fieldError.getDefaultMessage()
                                        : "Invalid value"
                        )
                );

        return ResponseEntity.badRequest().body(errors);
    }

    // CONVERT THE SPRING BOOT RESPONSE TO USER UNDERSTANDABLE MESSAGE
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseExceptions(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();

        String message = ex.getMessage();

        // CHECK WHAT TYPE OF PARSING ERROR IT IS
        if (message != null && message.contains("LocalDate")) {
            error.put("error", "Invalid date format. Please use YYYY-MM-DD (example: 2000-01-15)");
        } else if (message != null && message.contains("Cannot deserialize")) {
            error.put("error", "Invalid data format. Please check your input.");
        } else {
            error.put("error", "Invalid request format. Please check your data.");
        }

        return ResponseEntity.badRequest().body(error);
    }

//    email exception handler(email already exist)

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", "Email address already exists");

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }


} // END OF CLASS