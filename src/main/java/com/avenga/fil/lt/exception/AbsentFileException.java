package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class AbsentFileException extends LambdaException {

    public AbsentFileException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
