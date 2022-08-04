package com.ytvideoshare.backend.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)  // redirects all MethodArgumentNotValidException exception to this method
    public Map<String , String> handleInvalidArgument(MethodArgumentNotValidException ex){
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)  // redirects all MethodArgumentNotValidException exception to this method
    public Map<String , String> handleDuplicateEmailException(DuplicateEmailException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error",ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFound.class)  // redirects all MethodArgumentNotValidException exception to this method
    public Map<String , String> handleResourceNotFound(ResourceNotFound ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error",ex.getMessage());
        return errorMap;
    }

}
