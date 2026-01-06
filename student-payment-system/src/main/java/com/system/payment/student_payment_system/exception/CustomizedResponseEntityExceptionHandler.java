package com.system.payment.student_payment_system.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) throws Exception {

        return new ResponseEntity<>(getErrorDetails(ex, request), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(StudentNotFoundException.class)
    public final ResponseEntity<?> handleStudentNotFoundException(StudentNotFoundException ex, WebRequest request) throws Exception {
        return new ResponseEntity<>(getErrorDetails(ex, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public final ResponseEntity<?> handleTransactionNotFoundException(TransactionNotFoundException ex, WebRequest request) throws Exception {
        return new ResponseEntity<>(getErrorDetails(ex, request), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getFieldError().getDefaultMessage(), request.getDescription(false));


        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    private static ErrorDetails getErrorDetails(Exception ex, WebRequest request) {
        return new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
    }
}
