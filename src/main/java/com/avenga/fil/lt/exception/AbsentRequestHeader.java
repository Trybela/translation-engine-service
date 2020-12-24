package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class AbsentRequestHeader extends LambdaException {

    public AbsentRequestHeader(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
