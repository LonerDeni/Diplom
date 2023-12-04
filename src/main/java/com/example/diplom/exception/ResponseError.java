package com.example.diplom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseError {
    private Integer id;
    private String message;
}