package com.example.testingspringapp.config;

import com.example.testingspringapp.model.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Configuration
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        String errorMsg = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        final ErrorResponse errorResponse = buildErrorResponse(errorMsg);
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex){
        log.error(ex.getMessage());
        final String validationErrorMessage = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).findFirst().orElse(ex.getMessage());
        final var errorResponse = new ErrorResponse(validationErrorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
    /**
     * Build error response along with error codes
     * Current custom validators do not support error codes along with error message.
     * Error message should be separated by ::, if error code exists
     *  Eg, 1. "100::Test Error Message" - response with error code 100 and error message as Test Error Message
     *      2. "Test Error Message" - response with error message Test Error Message
     * @param errorMsg - :: separated errorCode and errorMessage
     * @return - Error response with Error Code(if exists), Error Message and HTTP Status
     */
    private ErrorResponse buildErrorResponse(String errorMsg) {
        String[] errorMsgSplit = errorMsg.split("::");
        Integer errorCode = null;
        String errorMessage = null;
        if (errorMsgSplit.length == 1) {
            errorMessage = errorMsgSplit[0];
        }
        if (errorMsgSplit.length == 2) {
            errorCode = Integer.valueOf(errorMsgSplit[0]);
            errorMessage = errorMsgSplit[1];
        }

        return new ErrorResponse(errorMessage, BAD_REQUEST, errorCode);
    }
}
