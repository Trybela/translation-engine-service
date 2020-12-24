package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class AbsentRequestQueryParameter extends LambdaException {

    public AbsentRequestQueryParameter(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
