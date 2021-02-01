package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class ExcelFormationException extends LambdaException {

    public ExcelFormationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
