package com.avenga.fil.lt.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.avenga.fil.lt.data.RequestPayloadData;

public interface RequestParserService {

    RequestPayloadData parseAndPreparePayload(APIGatewayProxyRequestEvent event);
}
