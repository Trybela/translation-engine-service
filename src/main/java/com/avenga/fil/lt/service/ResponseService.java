package com.avenga.fil.lt.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public interface ResponseService {

    APIGatewayProxyResponseEvent createErrorResponse(Throwable throwable);

    APIGatewayProxyResponseEvent createSuccessResponse();
}
