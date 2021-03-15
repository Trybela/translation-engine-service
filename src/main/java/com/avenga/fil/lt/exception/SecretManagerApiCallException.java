package com.avenga.fil.lt.exception;

import org.springframework.http.HttpStatus;

public class SecretManagerApiCallException extends LambdaException {
    public SecretManagerApiCallException(String source) {
        super(String.format("Error while calling Secrets Manager API. Message: %s", source), HttpStatus.BAD_REQUEST);
    }
}
