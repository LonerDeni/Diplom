package com.example.diplom.advice;

import com.example.diplom.exception.AuthTokenException;
import com.example.diplom.exception.CreateUserException;
import com.example.diplom.exception.FileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> credentialsHandler(FileException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthTokenException.class)
    public ResponseEntity<String> credentialsHandler(AuthTokenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<String> credentialsHandler(CreateUserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
