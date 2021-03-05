package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.avenga.fil.lt.config.property.SupportedValues;
import com.avenga.fil.lt.data.RequestPayloadData;
import com.avenga.fil.lt.exception.AbsentRequestHeader;
import com.avenga.fil.lt.exception.AbsentRequestQueryParameter;
import com.avenga.fil.lt.exception.EmptyRequestHeader;
import com.avenga.fil.lt.exception.UnsupportedFileTypeException;
import com.avenga.fil.lt.service.RequestParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.avenga.fil.lt.constant.ApiEventConstants.*;
import static com.avenga.fil.lt.constant.GeneralConstants.*;

@Service
@RequiredArgsConstructor
public class RequestParserServiceImpl implements RequestParserService {

    private static final int NEXT_INDEX = 1;

    private final SupportedValues supportedValues;

    @Override
    public RequestPayloadData parseAndPreparePayload(APIGatewayProxyRequestEvent event) {
        var userId = parseUserId(event.getHeaders());
        var queryParams = validateRequestQueryParameter(event.getQueryStringParameters());
        return constructPayloadData(userId, queryParams);
    }

    private RequestPayloadData constructPayloadData(String userId, Map<String, String> queryParams) {
        return RequestPayloadData.builder()
                .fileType(parseAndValidateFileType(queryParams.get(DOCUMENT_NAME)))
                .documentName(queryParams.get(DOCUMENT_NAME))
                .fromLanguage(queryParams.get(FROM_LANGUAGE))
                .toLanguage(queryParams.get(TO_LANGUAGE))
                .userId(userId)
                .unit(queryParams.get(BUSINESS_UNIT))
                .applyXlsRules(parseAndValidateApplyXlsRules(queryParams.get(APPLY_XLS_RULES)))
                .xlsColumns(parseAndValidateXlsColumns(queryParams.get(XLS_COLUMNS)))
                .build();
    }

    private String parseUserId(Map<String, String> headers) {
        if (headers == null || !headers.containsKey(USER_ID)) {
            throw new AbsentRequestHeader(String.format(ABSENT_REQUEST_HEADER_ERROR_MESSAGE, USER_ID));
        }
        return Optional.ofNullable(headers.get(USER_ID)).filter(s -> !s.isBlank()).orElseThrow(() -> new EmptyRequestHeader(String.format(EMPTY_REQUEST_HEADER_ERROR_MESSAGE, USER_ID)));
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

    private boolean parseAndValidateApplyXlsRules(String applyXlsRules) {
        return Boolean.parseBoolean(applyXlsRules);
    }

    private String parseAndValidateXlsColumns(String xlsColumns) {
        if (!StringUtils.hasText(xlsColumns)) {
            return null;
        }
        xlsColumns = xlsColumns.replaceAll("\\s+", "");
        if (!validate(xlsColumns)) {
            throw new UnsupportedFileTypeException(String.format(WRONG_INPUT_FORMAT_ERROR_MESSAGE, xlsColumns));
        }
        return convertAndNormalize(xlsColumns);
    }

    private String convertAndNormalize(String xlsColumns) {
        return Stream.of(xlsColumns.split(","))
                .map(String::trim)
                .map(s -> s.contains("-") ? convertRangeToList(s) : List.of(Integer.parseInt(s)))
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private List<Integer> convertRangeToList(String range) {
        String[] rangeArray = range.split("-");
        int startNumber = Integer.parseInt(rangeArray[0]);
        int endNumber = Integer.parseInt(rangeArray[1]);
        return IntStream.rangeClosed(startNumber, endNumber).boxed().collect(Collectors.toList());
    }

    private boolean validate(String xlsColumns) {
        // allow only numbers, -(dash) and ,(comma) in any order
        String format = "[0-9]+(?:-[0-9]+)?(,[0-9]+(?:-[0-9]+)?)*";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(xlsColumns);
        return matcher.matches();
    }
}
