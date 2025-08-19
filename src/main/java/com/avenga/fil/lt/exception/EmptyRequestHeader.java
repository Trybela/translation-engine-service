package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class EmptyRequestHeader extends LambdaException {

    public EmptyRequestHeader(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
