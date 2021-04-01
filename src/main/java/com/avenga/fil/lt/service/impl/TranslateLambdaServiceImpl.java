package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.data.*;
import com.avenga.fil.lt.exception.AbsentFileException;
import com.avenga.fil.lt.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

import static com.avenga.fil.lt.constant.GeneralConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateLambdaServiceImpl implements TranslateLambdaService {

    private final RequestParserService parserService;
    private final S3Service s3Service;
    private final ResponseService responseService;
    private final TextExtractService textExtractService;
    private static final String DATE_TIME_PATTERN = "yyyyMMddHHmmss";
    private static final String FILE_NAME_DELIMITER = "_";
    private static final String URL_PREFIX = "https://sandbox.api.fil.com/language-translator/v1/status?translatedDocumentName=";

    @Override
    public APIGatewayProxyResponseEvent processRequest(APIGatewayProxyRequestEvent event) {
        log.info(INCOMING_REQUEST_LOG_MESSAGE);
        return process(event);
    }

    private APIGatewayProxyResponseEvent process(APIGatewayProxyRequestEvent event) {
        try {
            var payloadData = parserService.parseAndPreparePayload(event);
            var storageData = getFileFromS3(payloadData);
            var fileName = constructFileName(payloadData.getDocumentName() + TRANSLATED, payloadData.getFileType(), payloadData.getUserId());
            invokingExtractTextProcess(fileName, payloadData, storageData);
            var statusEntity = getStatus(fileName);
            return responseService.createSuccessResponse(statusEntity);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return responseService.createErrorResponse(exception);
        }
    }

    private FileStorageData getFileFromS3(RequestPayloadData payloadData) {
        if (!s3Service.isFileExists(payloadData.getDocumentName())) {
            throw new AbsentFileException(String.format(ABSENT_FILE_ON_S3_BUCKET_ERROR_MESSAGE, payloadData.getDocumentName()));
        }
        var storageData = s3Service.getFile(payloadData.getDocumentName());
        log.info(FILE_IS_PRESENT_ON_S3);
        return storageData;
    }

    private void invokingExtractTextProcess(String fileName, RequestPayloadData requestPayloadData, FileStorageData storageData) {
        textExtractService.extractText(new TextExtractInput(fileName, storageData, requestPayloadData));
        log.info(TEXT_EXTRACT_LAMBDA_INVOKED);
    }

    private String constructFileName(String fileName, String fileType, String userId) {
        var dateTime = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now());
        var pureFileName = fileName.substring(0, fileName.lastIndexOf(EXTENSION_DELIMITER));
        return new StringJoiner(FILE_NAME_DELIMITER).add(pureFileName).add(userId).add(dateTime).toString() +
                EXTENSION_DELIMITER + fileType;
    }

    private Status getStatus(String fileName) {
        return Status.builder()
                .translatedDocumentName(fileName)
                .links(List.of(Link.builder().href(URL_PREFIX + fileName).build()))
                .build();
    }
}
