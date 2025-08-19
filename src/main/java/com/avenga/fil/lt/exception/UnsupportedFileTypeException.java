package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class UnsupportedFileTypeException extends LambdaException {

    public UnsupportedFileTypeException(String message) {
        super(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
