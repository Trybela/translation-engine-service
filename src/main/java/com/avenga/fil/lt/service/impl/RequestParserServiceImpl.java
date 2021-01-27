package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.avenga.fil.lt.config.properties.SupportedValues;
import com.avenga.fil.lt.data.RequestPayloadData;
import com.avenga.fil.lt.exception.*;
import com.avenga.fil.lt.service.RequestParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.avenga.fil.lt.constants.ApiEventConstants.*;
import static com.avenga.fil.lt.constants.GeneralConstants.*;

@Service
@RequiredArgsConstructor
public class RequestParserServiceImpl implements RequestParserService {

    private static final int FIRST_INDEX = 0;
    private static final int NEXT_INDEX = 1;

    private final SupportedValues supportedValues;

    @Override
    public RequestPayloadData parseAndPreparePayload(APIGatewayProxyRequestEvent event) {
        var contentType = parseContentType(Optional.ofNullable(event.getHeaders())
                .orElseThrow(() -> new AbsentRequestHeader(ABSENT_REQUEST_HEADER_ERROR_MESSAGE)));
        var queryParams = validateRequestQueryParameter(event.getQueryStringParameters());
        return constructPayloadData(contentType, queryParams, event.getBody());
    }

    private RequestPayloadData constructPayloadData(String contentType, Map<String, String> queryParams, String body) {
        return RequestPayloadData.builder()
                .contentType(contentType)
                .fileType(parseAndValidateFileType(queryParams.get(FILE_NAME)))
                .fileName(queryParams.get(FILE_NAME))
                .fromLanguage(queryParams.get(FROM_LANGUAGE))
                .toLanguage(queryParams.get(TO_LANGUAGE))
                .userId(queryParams.get(USER_ID))
                .body(parseAndValidateBody(body))
                .build();
    }

    private String parseContentType(Map<String, String> headers) {
        if (!supportedValues.getContentTypes().contains(headers.getOrDefault(CONTENT_TYPE, NOT_EXIST_DEFAULT_VALUE)))
            throw new UnsupportedContentTypeException(
                    String.format(UNSUPPORTED_CONTENT_TYPE_ERROR_MESSAGE, headers.get(CONTENT_TYPE)));

        return headers.get(CONTENT_TYPE);
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

    private String parseAndValidateBody(String body) {
        return Optional.ofNullable(body).orElseThrow(() -> new AbsentRequestBody(ABSENT_REQUEST_BODY_ERROR_MESSAGE));
    }
}
