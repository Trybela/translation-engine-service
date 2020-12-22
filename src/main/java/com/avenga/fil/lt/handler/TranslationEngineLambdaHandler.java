package com.avenga.fil.lt.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class TranslationEngineLambdaHandler
        extends SpringBootRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {}
