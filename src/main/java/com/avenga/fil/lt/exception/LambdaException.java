package com.avenga.fil.lt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LambdaException extends RuntimeException {

    private final HttpStatus httpStatus;

    public LambdaException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public LambdaException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
