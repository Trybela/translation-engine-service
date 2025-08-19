package com.avenga.fil.lt.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.data.Status;

public interface ResponseService {

    APIGatewayProxyResponseEvent createErrorResponse(Exception exception);

    APIGatewayProxyResponseEvent createSuccessResponse(Status status);
}
