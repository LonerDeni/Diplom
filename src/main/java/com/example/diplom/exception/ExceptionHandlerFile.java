package com.example.diplom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerFile {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError credentialsHandler(FileException e) {
        return new ResponseError(null,e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AuthTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handlerException(AuthTokenException e) {
        return new ResponseError(null,e.getMessage());
    }
}
