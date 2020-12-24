package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class AbsentRequestBody extends LambdaException {

    public AbsentRequestBody(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
