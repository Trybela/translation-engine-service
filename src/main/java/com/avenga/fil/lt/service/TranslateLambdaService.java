package com.avenga.fil.lt.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public interface TranslateLambdaService {

    APIGatewayProxyResponseEvent processRequest(APIGatewayProxyRequestEvent event);
}
