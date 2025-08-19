package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.data.MoreInfo;
import com.avenga.fil.lt.data.Status;
import com.avenga.fil.lt.exception.LambdaException;
import com.avenga.fil.lt.service.ResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.avenga.fil.lt.constant.ApiEventConstants.CONTENT_TYPE;
import static com.avenga.fil.lt.constant.GeneralConstants.ERROR_RESPONSE_FORMAT;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent createErrorResponse(Exception exception) {
        var httpStatus = determineHttpStatus(exception);
        return createResponse(getString(getMoreInfo(exception, httpStatus)), httpStatus);
    }

    @Override
    public APIGatewayProxyResponseEvent createSuccessResponse(Status status) {
        return createResponse(getString(status), HttpStatus.CREATED);
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

    private MoreInfo getMoreInfo(Exception exception, HttpStatus statusCode) {
        String userMessage = String.format(ERROR_RESPONSE_FORMAT, exception.getMessage());
        return MoreInfo.builder()
                .code(String.valueOf(statusCode.value()))
                .userMessage(userMessage)
                .developerMessage(List.of(exception.toString()))
                .moreInfoMessage(exception.toString())
                .build();
    }

    private String getString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return exception.getMessage();
        }
    }
}
