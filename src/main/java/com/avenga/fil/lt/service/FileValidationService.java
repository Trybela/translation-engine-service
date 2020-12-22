package com.avenga.fil.lt.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public interface FileValidationService {

    void validation(APIGatewayProxyRequestEvent event);
}
