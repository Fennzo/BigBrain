package com.bigbrain.v1.exceptionhandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.Date;

// Handles sql server user input error. When an error is received by sql server, it returns JSON to react
@RestControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

    // Handling IllegalArgumentException with an error message and HTTP status code of 400 Bad Request
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        String errorMessage = "Invalid input parameters";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handling Exception with a generic error message and HTTP status code of 500 Internal Server Error
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        String errorMessage = "Internal Server Error";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*Handling SQLException with different error messages and HTTP status codes based on the error code.
    If the error code is 547, it returns a message that indicates a foreign key constraint violation, with a status code of 409 Conflict.
    If the error code is 2627 or 2601, it returns a message that indicates a duplicate entry or constraint violation, also with a status code of 409 Conflict.
    For any other error code, it returns a message with the SQL error message and HTTP status code of 500 Internal Server Error.
     */
    @ExceptionHandler(value = { SQLException.class })
    public ResponseEntity<Object> handleSQLException(SQLException ex) {
        if (ex.getErrorCode() == 547) {
            return new ResponseEntity<>("Cannot delete or update a parent row: a foreign key constraint fails", HttpStatus.CONFLICT);
        }
        else if (ex.getErrorCode() == 2627 || ex.getErrorCode() == 2601) {
            return new ResponseEntity<>("Duplicate entry or constraint violation", HttpStatus.CONFLICT);
        }
        else {
            return new ResponseEntity<>("SQL error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    Handling DataIntegrityViolationException with different error messages and HTTP status codes based on the type of constraint violation.
    If the constraint name contains "_check", it returns a message that indicates a check constraint violation, with a status code of 409 Conflict.
    If the constraint name contains with "_trigger", it returns a message that indicates a trigger error, also with a status code of 409 Conflict.
    For any other constraint violation, it returns a message with the constraint violation error message and HTTP status code of 409 Conflict.
    If the cause of the exception is not a ConstraintViolationException, it returns a message that indicates a data integrity violation with HTTP status code of 409 Conflict
     */
    // return errordetails object
    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex.getCause();
            String constraintName = "";
            for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
                constraintName = cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
                break;
            }
            if (constraintName.contains("_check")) {
                return new ResponseEntity<>("Check constraint violation", HttpStatus.CONFLICT);
            }
            else if (constraintName.contains("_trigger")) {
                return new ResponseEntity<>("Trigger error", HttpStatus.CONFLICT);
            }
            else {
                return new ResponseEntity<>("Constraint violation: " + cve.getMessage(), HttpStatus.CONFLICT);
            }
        }
        else {
            return new ResponseEntity<>("Data integrity violation: " + ex.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
