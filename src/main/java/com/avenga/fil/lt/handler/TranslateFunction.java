package com.avenga.fil.lt.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.service.TranslateLambdaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranslateFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final TranslateLambdaService translateLambdaService;

    @Override
    public  APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent event) {
        return translateLambdaService.processRequest(event);
    }
}
