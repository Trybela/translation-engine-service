package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class WrongInputFormatException extends LambdaException {

    public WrongInputFormatException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
