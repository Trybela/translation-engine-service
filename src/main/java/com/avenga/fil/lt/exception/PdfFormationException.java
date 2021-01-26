package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class PdfFormationException extends LambdaException {

    public PdfFormationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
