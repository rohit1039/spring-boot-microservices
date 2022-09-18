package com.services.orderservice.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException ex, WebRequest request)
    {
        ErrorResponse exceptionInResponse = ErrorResponse
            .builder()
            .timestamp(LocalDateTime.now())
            .errorMessage(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .statusCode(ex.getStatusCode())
            .build();

        return new ResponseEntity<>(exceptionInResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        final Map<String, String> errors = new ConcurrentHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach((error) ->
        {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        exception.getBindingResult().getGlobalErrors().forEach((errorGlobal) ->
        {
            errors.put(((FieldError) errorGlobal).getField(), errorGlobal.getDefaultMessage());
        });
        log.error("{}", errors);
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }
}
