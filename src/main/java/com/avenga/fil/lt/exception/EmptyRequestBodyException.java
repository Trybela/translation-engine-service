package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class EmptyRequestBodyException extends LambdaException {

    public EmptyRequestBodyException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
