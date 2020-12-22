package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class UnsupportedContentTypeException extends LambdaException {

    public UnsupportedContentTypeException(String message) {
        super(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
