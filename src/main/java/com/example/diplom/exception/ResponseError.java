package com.example.diplom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseError {
    private Long id;
    private String message;
}