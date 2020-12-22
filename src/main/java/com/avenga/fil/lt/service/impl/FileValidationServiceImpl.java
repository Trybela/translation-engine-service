package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.avenga.fil.lt.config.properties.SupportedValues;
import com.avenga.fil.lt.exception.UnsupportedContentTypeException;
import com.avenga.fil.lt.exception.UnsupportedFileTypeException;
import com.avenga.fil.lt.service.FileValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.avenga.fil.lt.constants.ApiEventConstants.CONTENT_TYPE;
import static com.avenga.fil.lt.constants.ApiEventConstants.FILE_NAME;
import static com.avenga.fil.lt.constants.GeneralConstants.UNSUPPORTED_CONTENT_TYPE_ERROR_MESSAGE;
import static com.avenga.fil.lt.constants.GeneralConstants.UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class FileValidationServiceImpl implements FileValidationService {

    public static final String EXTENSION_DELIMITER = ".";
    public static final int NEXT_INDEX = 1;

    private final SupportedValues supportedValues;

    @Override
    public void validation(APIGatewayProxyRequestEvent event) {
        validateSupportedFieType(event.getQueryStringParameters().get(FILE_NAME));
        validateSupportedContentType(event.getHeaders().get(CONTENT_TYPE));
    }

    private void validateSupportedContentType(String contentType) {
        if (!supportedValues.getContentTypes().contains(contentType))
            throw new UnsupportedContentTypeException(String.format(UNSUPPORTED_CONTENT_TYPE_ERROR_MESSAGE, contentType));
    }

    private void validateSupportedFieType(String fileName) {
        var fileType = fileName.substring(fileName.lastIndexOf(EXTENSION_DELIMITER) + NEXT_INDEX);
        if(!(supportedValues.getExtensions().contains(fileType))) {
            throw new UnsupportedFileTypeException(String.format(UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE, fileType));
        }
    }
}
