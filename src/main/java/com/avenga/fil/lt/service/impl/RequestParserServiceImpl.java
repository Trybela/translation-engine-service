package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.avenga.fil.lt.config.property.SupportedValues;
import com.avenga.fil.lt.data.RequestPayloadData;
import com.avenga.fil.lt.exception.AbsentRequestQueryParameter;
import com.avenga.fil.lt.exception.UnsupportedFileTypeException;
import com.avenga.fil.lt.service.RequestParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.avenga.fil.lt.constant.ApiEventConstants.*;
import static com.avenga.fil.lt.constant.GeneralConstants.*;

@Service
@RequiredArgsConstructor
public class RequestParserServiceImpl implements RequestParserService {

    private static final int NEXT_INDEX = 1;

    private final SupportedValues supportedValues;

    @Override
    public RequestPayloadData parseAndPreparePayload(APIGatewayProxyRequestEvent event) {
        var queryParams = validateRequestQueryParameter(event.getQueryStringParameters());
        return constructPayloadData(queryParams);
    }

    private RequestPayloadData constructPayloadData(Map<String, String> queryParams) {
        return RequestPayloadData.builder()
                .fileType(parseAndValidateFileType(queryParams.get(DOCUMENT_NAME)))
                .documentName(queryParams.get(DOCUMENT_NAME))
                .fromLanguage(queryParams.get(FROM_LANGUAGE))
                .toLanguage(queryParams.get(TO_LANGUAGE))
                .userId(queryParams.get(USER_ID))
                .build();
    }

    private Map<String, String> validateRequestQueryParameter(Map<String, String> queryParameters) {
        if (queryParameters == null || !queryParameters.keySet().containsAll(supportedValues.getQueryParameters())) {
            throw new AbsentRequestQueryParameter(ABSENT_REQUEST_QUERY_PARAM_ERROR_MESSAGE);
        }
        return queryParameters;
    }

    private String parseAndValidateFileType(String fileName) {
        var fileType = fileName.substring(fileName.lastIndexOf(EXTENSION_DELIMITER) + NEXT_INDEX);
        if (!(supportedValues.getExtensions().contains(fileType))) {
            throw new UnsupportedFileTypeException(String.format(UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE, fileType));
        }
        return fileType;
    }
}
