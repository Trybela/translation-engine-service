package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class RequestBodyParsingException extends LambdaException {

    public RequestBodyParsingException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
