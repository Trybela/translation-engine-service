package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.exception.LambdaException;
import com.avenga.fil.lt.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.avenga.fil.lt.constants.ApiEventConstants.CONTENT_TYPE;
import static com.avenga.fil.lt.constants.GeneralConstants.ERROR_RESPONSE_FORMAT;
import static com.avenga.fil.lt.constants.GeneralConstants.SUCCESS_RESPONSE;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Override
    public APIGatewayProxyResponseEvent createErrorResponse(Exception exception) {
        return createResponse(String.format(ERROR_RESPONSE_FORMAT, exception.getMessage()), determineHttpStatus(exception));
    }

    @Override
    public APIGatewayProxyResponseEvent createSuccessResponse() {
        return createResponse(SUCCESS_RESPONSE, HttpStatus.OK);
    }

    private HttpStatus determineHttpStatus(Throwable throwable) {
        return throwable instanceof LambdaException
                ? ((LambdaException) throwable).getHttpStatus()
                : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private APIGatewayProxyResponseEvent createResponse(String body, HttpStatus statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withBody(body)
                .withHeaders(Map.of(CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .withStatusCode(statusCode.value());
    }
}
