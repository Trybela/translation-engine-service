package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class AbsentRequestBodyParameter extends LambdaException {

    public AbsentRequestBodyParameter(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
