package com.babakjan.moneybag.exception;

import com.babakjan.moneybag.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({
            AccountNotFoundException.class,
            CategoryNotFoundException.class,
            RecordNotFoundException.class,
            UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage entityNotFoundException(Exception exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("entity", exception.getMessage());
        return new ErrorMessage(HttpStatus.NOT_FOUND, errors);
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage userAlreadyExistsException(Exception exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("email", exception.getMessage());
        return new ErrorMessage(HttpStatus.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage invalidArgumentException(MethodArgumentNotValidException exception) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST);
        exception.getBindingResult().getFieldErrors().forEach(error ->
            errorMessage.getErrors().put(error.getField(), error.getDefaultMessage())
        );
        return errorMessage;
    }
}
