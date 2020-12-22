package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.service.FileValidationService;
import com.avenga.fil.lt.service.S3Service;
import com.avenga.fil.lt.service.TranslateLambdaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.avenga.fil.lt.constants.ApiEventConstants.CONTENT_TYPE;
import static com.avenga.fil.lt.constants.ApiEventConstants.FILE_NAME;
import static com.avenga.fil.lt.constants.GeneralConstants.FILE_SUCCESSFULLY_UPLOADED;
import static com.avenga.fil.lt.constants.GeneralConstants.INCOMING_REQUEST_LOG_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateLambdaServiceImpl implements TranslateLambdaService {

    private final FileValidationService validationService;
    private final S3Service s3Service;

    @Override
    public APIGatewayProxyResponseEvent processRequest(APIGatewayProxyRequestEvent event) {
        log.info(INCOMING_REQUEST_LOG_MESSAGE);
        return process(event);
    }

    private APIGatewayProxyResponseEvent process(APIGatewayProxyRequestEvent event) {
        validationService.validation(event);
        saveFileToS3(event);
        return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatus.OK.value());
    }

    private void saveFileToS3(APIGatewayProxyRequestEvent event) {
        s3Service.saveFile(event.getQueryStringParameters().get(FILE_NAME),
                Base64.decodeBase64(event.getBody().getBytes()),
                event.getHeaders().get(CONTENT_TYPE)
        );
        log.info(FILE_SUCCESSFULLY_UPLOADED);
    }
}
