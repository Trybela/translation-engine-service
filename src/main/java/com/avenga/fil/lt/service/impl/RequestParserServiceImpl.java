package com.avenga.fil.lt.service.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.avenga.fil.lt.config.property.SupportedValues;
import com.avenga.fil.lt.data.RequestPayloadData;
import com.avenga.fil.lt.exception.*;
import com.avenga.fil.lt.service.RequestParserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.avenga.fil.lt.constant.ApiEventConstants.*;
import static com.avenga.fil.lt.constant.GeneralConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestParserServiceImpl implements RequestParserService {

    private static final int NEXT_INDEX = 1;

    private final SupportedValues supportedValues;
    private final ObjectMapper objectMapper;

    @Override
    public RequestPayloadData parseAndPreparePayload(APIGatewayProxyRequestEvent event) {
        var userId = parseUserId(event.getHeaders());
        var requestBodyParameters = validateRequestBodyParameters(event.getBody());
        return constructPayloadData(userId, requestBodyParameters);
    }

    private RequestPayloadData constructPayloadData(String userId, Map<String, String> requestBodyParameters) {
        return RequestPayloadData.builder()
                .fileType(parseAndValidateFileType(requestBodyParameters.get(DOCUMENT_NAME)))
                .documentName(requestBodyParameters.get(DOCUMENT_NAME))
                .fromLanguage(validateLanguageCode(requestBodyParameters.get(FROM_LANGUAGE)))
                .toLanguage(validateLanguageCode(requestBodyParameters.get(TO_LANGUAGE)))
                .userId(userId)
                .unit(requestBodyParameters.get(BUSINESS_UNIT))
                .applyXlsRules(parseAndValidateApplyXlsRules(requestBodyParameters.get(APPLY_XLS_RULES)))
                .xlsColumns(parseAndValidateXlsColumns(requestBodyParameters.get(XLS_COLUMNS)))
                .build();
    }

    private String parseUserId(Map<String, String> headers) {
        if (headers == null || !headers.containsKey(USER_ID)) {
            throw new AbsentRequestHeader(String.format(ABSENT_REQUEST_HEADER_ERROR_MESSAGE, USER_ID));
        }
        return Optional.ofNullable(headers.get(USER_ID)).filter(s -> !s.isBlank())
                .orElseThrow(() -> new EmptyRequestHeader(String.format(EMPTY_REQUEST_HEADER_ERROR_MESSAGE, USER_ID)));
    }

    private Map<String, String> validateRequestBodyParameters(String body) {
        try {
            if (!StringUtils.hasText(body)) {
                throw new EmptyRequestBodyException(EMPTY_REQUEST_BODY);
            }
            return validateMandatoryRequestBodyParameters(objectMapper.readValue(body, new TypeReference<>() {}));
        } catch (Exception e) {
            throw new RequestBodyParsingException(String.format(REQUEST_BODY_PARSING_ERROR_MESSAGE, e.getMessage()));
        }
    }

    private Map<String, String> validateMandatoryRequestBodyParameters(Map<String, String> parameters) {
        if (!parameters.keySet().containsAll(supportedValues.getRequestBodyParameters())) {
            throw new AbsentRequestBodyParameter(ABSENT_REQUEST_BODY_PARAM_ERROR_MESSAGE);
        }
        return parameters;
    }

    private String parseAndValidateFileType(String fileName) {
        var fileType = fileName.substring(fileName.lastIndexOf(EXTENSION_DELIMITER) + NEXT_INDEX);
        if (!(supportedValues.getExtensions().contains(fileType))) {
            throw new UnsupportedFileTypeException(String.format(UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE, fileType));
        }
        return fileType;
    }

    private String validateLanguageCode(String languageCode) {
        if (!(supportedValues.getLanguageCodes().contains(languageCode))) {
            throw new UnsupportedLanguageCodeException(String.format(UNSUPPORTED_LANGUAGE_CODE_ERROR_MESSAGE, languageCode));
        }
        return languageCode;
    }

    private boolean parseAndValidateApplyXlsRules(String applyXlsRules) {
        return Boolean.parseBoolean(applyXlsRules);
    }

    private String parseAndValidateXlsColumns(String xlsColumns) {
        if (!StringUtils.hasText(xlsColumns)) {
            return null;
        }
        xlsColumns = xlsColumns.replaceAll("\\s+", "").toUpperCase();
        if (!validate(xlsColumns)) {
            throw new UnsupportedFileTypeException(String.format(WRONG_INPUT_FORMAT_ERROR_MESSAGE, xlsColumns));
        }
        return convertAndNormalize(xlsColumns);
    }

    private String convertAndNormalize(String xlsColumns) {
        return Stream.of(xlsColumns.split(","))
                .map(String::trim)
                .map(s -> s.contains("-") ? convertRangeToList(s) : List.of(getExcelColumnNumber(s)))
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private List<Integer> convertRangeToList(String range) {
        String[] rangeArray = range.split("-");
        int startNumber = getExcelColumnNumber(rangeArray[0]);
        int endNumber = getExcelColumnNumber(rangeArray[1]);
        return IntStream.rangeClosed(startNumber, endNumber).boxed().collect(Collectors.toList());
    }

    private boolean validate(String xlsColumns) {
        // allow only capital letters, -(dash) and ,(comma) in any order
        String format = "(([A-Z]){1,3}(-([A-Z]){1,3})?)(,([A-Z]){1,3}(-([A-Z]){1,3})?)*";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(xlsColumns);
        return matcher.matches();
    }

    private int getExcelColumnNumber(String column) {
        int result = 0;
        for (int i = 0; i < column.length(); i++) {
            result *= 26;
            result += column.charAt(i) - 'A' + 1;
        }
        return result;
    }
}
