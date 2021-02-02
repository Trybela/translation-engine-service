package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class TxtFormationException extends LambdaException {

    public TxtFormationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
