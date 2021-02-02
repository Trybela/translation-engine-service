package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class TextTranslateProcessException extends LambdaException {

    public TextTranslateProcessException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
