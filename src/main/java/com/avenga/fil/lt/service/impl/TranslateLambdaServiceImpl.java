package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.avenga.fil.lt.data.FileStorageData;
import com.avenga.fil.lt.data.RequestPayloadData;
import com.avenga.fil.lt.data.TextExtractInput;
import com.avenga.fil.lt.exception.AbsentFileException;
import com.avenga.fil.lt.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.avenga.fil.lt.constants.GeneralConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateLambdaServiceImpl implements TranslateLambdaService {

    private final RequestParserService parserService;
    private final S3Service s3Service;
    private final ResponseService responseService;
    private final TextExtractService textExtractService;
    private final DocumentFormationService documentFormationService;

    @Override
    public APIGatewayProxyResponseEvent processRequest(APIGatewayProxyRequestEvent event) {
        log.info(INCOMING_REQUEST_LOG_MESSAGE);
        return process(event);
    }

    private APIGatewayProxyResponseEvent process(APIGatewayProxyRequestEvent event) {
        try {
            var payloadData = parserService.parseAndPreparePayload(event);
            var storageData = getFileFromS3(payloadData);
            var extractedContent = extractText(payloadData.getFileType(), storageData);
            var byteDocument = documentFormationService.formation(payloadData.getFileType(), extractedContent);
            saveFileToS3(byteDocument, payloadData);
            return responseService.createSuccessResponse();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            return responseService.createErrorResponse(throwable);
        }
    }

    private FileStorageData getFileFromS3(RequestPayloadData payloadData) {
        if (!s3Service.isFileExists(payloadData.getFileName())) {
            throw new AbsentFileException(String.format(ABSENT_FILE_ON_S3_BUCKET_ERROR_MESSAGE, payloadData.getFileName()));
        }
        var storageData = s3Service.getFile(payloadData.getFileName());
        log.info(FILE_IS_PRESENT_ON_S3);
        return storageData;
    }

    private void saveFileToS3(byte[] byteDocument, RequestPayloadData payloadData) {
        s3Service.saveFile(payloadData.getFileName() + TRANSLATED,
                payloadData.getFileType(), payloadData.getUserId(), byteDocument, payloadData.getContentType());
    }

    private String extractText(String fileType, FileStorageData storageData) {
        var pages = textExtractService.extractText(new TextExtractInput(storageData.getBucketName(),
                storageData.getFileKey(), fileType));
        log.info(TEXT_EXTRACT_ENDED);
        return pages;
    }
}
