package com.example.diplom.advice;

import cn.hutool.core.lang.Snowflake;
import com.example.diplom.exception.AuthTokenException;
import com.example.diplom.exception.CreateUserException;
import com.example.diplom.exception.FileException;
import com.example.diplom.exception.ResponseError;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Snowflake snowflake;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> credentialsHandler(FileException e) {
        return ResponseEntity.badRequest().body(new ResponseError(snowflake.nextId(),e.getMessage()));
    }

    @ExceptionHandler(AuthTokenException.class)
    public ResponseEntity<ResponseError> credentialsHandler(AuthTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseError(snowflake.nextId(),e.getMessage()));
    }

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<ResponseError> credentialsHandler(CreateUserException e) {
        return ResponseEntity.badRequest().body(new ResponseError(snowflake.nextId(),e.getMessage()));
    }
}
