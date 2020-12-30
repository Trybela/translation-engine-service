package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class TextExtractProcessException extends LambdaException {

    public TextExtractProcessException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
