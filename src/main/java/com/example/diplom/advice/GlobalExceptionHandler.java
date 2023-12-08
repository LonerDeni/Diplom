package com.example.diplom.advice;

import com.example.diplom.exception.AuthTokenException;
import com.example.diplom.exception.CreateUserException;
import com.example.diplom.exception.FileException;
import com.example.diplom.exception.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> credentialsHandler(FileException e) {
        //return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(new ResponseError(null,e.getMessage()));
    }

    @ExceptionHandler(AuthTokenException.class)
    public ResponseEntity<ResponseError> credentialsHandler(AuthTokenException e) {
        //return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseError(null,e.getMessage()));
    }

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<ResponseError> credentialsHandler(CreateUserException e) {
        //return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(new ResponseError(null,e.getMessage()));
    }
}
