package com.hs.healeon.exception;

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

} // END OF CLASS


// ============================================================
// STEP 1: GET THE LIST OF ERRORS
// ============================================================
// List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
//
// THIS LIST LOOKS LIKE:
// [
//   FieldError(field="email", message="Email is required"),
//   FieldError(field="password", message="Password too short"),
//   FieldError(field="phone", message="Phone is required")
// ]

// ============================================================
// STEP 2: CREATE AN EMPTY HASHMAP
// ============================================================
// Map<String, String> errors = new HashMap<>();

// ============================================================
// STEP 3: LOOP THROUGH EACH ERROR
// (THIS IS WHAT .stream().collect() DOES AUTOMATICALLY!)
// ============================================================
// for (FieldError fieldError : fieldErrorList) {

// --------------------------------------------------------
// GRABBING KEYS AND VALUES:
// --------------------------------------------------------
// ERROR 1: field = "email"     → KEY   |  message = "Email is required"    → VALUE
// ERROR 2: field = "password"  → KEY   |  message = "Password too short"   → VALUE
// ERROR 3: field = "phone"     → KEY   |  message = "Phone is required"    → VALUE

// --------------------------------------------------------
// STEP 4: EXTRACT THE KEY (FIELD NAME)
// --------------------------------------------------------
// String key = fieldError.getField();
// FIRST LOOP:  key = "email"
// SECOND LOOP: key = "password"
// THIRD LOOP:  key = "phone"

// --------------------------------------------------------
// STEP 5: EXTRACT THE VALUE (ERROR MESSAGE WITH NULL CHECK)
// --------------------------------------------------------
// String value;
// if (fieldError.getDefaultMessage() != null) {
//     value = fieldError.getDefaultMessage();
// } else {
//     value = "Invalid value";
// }
// FIRST LOOP:  value = "Email is required"
// SECOND LOOP: value = "Password too short"
// THIRD LOOP:  value = "Phone is required"

// --------------------------------------------------------
// STEP 6: PUT THE KEY-VALUE PAIR INTO THE HASHMAP
// --------------------------------------------------------
// errors.put(key, value);
// AFTER LOOP 1: errors = {"email": "Email is required"}
// AFTER LOOP 2: errors = {"email": "Email is required", "password": "Password too short"}
// AFTER LOOP 3: errors = {"email": "Email is required", "password": "Password too short", "phone": "Phone is required"}

// }

// ============================================================
// STEP 7: RETURN THE FILLED HASHMAP AS HTTP 400 BAD REQUEST
// ============================================================
// return ResponseEntity.badRequest().body(errors);
//
// FINAL RESPONSE SENT:
// HTTP 400 Bad Request
// {
//   "email": "Email is required",
//   "password": "Password too short",
//   "phone": "Phone is required"
// }

// ============================================================
// WHAT .stream().collect(Collectors.toMap()) DOES:
// ============================================================
// IT PERFORMS STEPS 2-6 AUTOMATICALLY IN ONE LINE:
// - CREATES EMPTY HASHMAP (STEP 2)
// - LOOPS THROUGH ERRORS (STEP 3)
// - EXTRACTS KEYS (STEP 4)
// - EXTRACTS VALUES (STEP 5)
// - PUTS KEY-VALUE PAIR