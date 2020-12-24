package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.data.RequestPayloadData;
import com.avenga.fil.lt.service.RequestParserService;
import com.avenga.fil.lt.service.S3Service;
import com.avenga.fil.lt.service.TranslateLambdaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.avenga.fil.lt.constants.GeneralConstants.FILE_SUCCESSFULLY_UPLOADED;
import static com.avenga.fil.lt.constants.GeneralConstants.INCOMING_REQUEST_LOG_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateLambdaServiceImpl implements TranslateLambdaService {

    private final RequestParserService parserService;
    private final S3Service s3Service;

    @Override
    public APIGatewayProxyResponseEvent processRequest(APIGatewayProxyRequestEvent event) {
        log.info(INCOMING_REQUEST_LOG_MESSAGE);
        return process(event);
    }

    private APIGatewayProxyResponseEvent process(APIGatewayProxyRequestEvent event) {
        RequestPayloadData payloadData = parserService.parseAndPreparePayload(event);
        saveFileToS3(payloadData);
        return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatus.OK.value());
    }

    private void saveFileToS3(RequestPayloadData payloadData) {
        s3Service.saveFile(payloadData.getFileName(), payloadData.getFileType(), payloadData.getUserId(),
                Base64.decodeBase64(payloadData.getBody().getBytes()), payloadData.getContentType());
        log.info(FILE_SUCCESSFULLY_UPLOADED);
    }
}
