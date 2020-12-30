package com.avenga.fil.lt.service.impl;

import com.avenga.fil.lt.data.TextExtractInput;
import com.avenga.fil.lt.exception.TextExtractProcessException;
import com.avenga.fil.lt.service.TextExtractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

import static com.avenga.fil.lt.constants.GeneralConstants.TEXT_EXTRACT_PROCESS_ERROR_MESSAGE;

@Slf4j
@Service
public class TextExtractServiceImpl implements TextExtractService {

    private final String functionName;
    private final LambdaClient lambdaClient;
    private final ObjectMapper objectMapper;

    public TextExtractServiceImpl(@Value("${textExtract.functionName}") String functionName, LambdaClient lambdaClient,
                                  ObjectMapper objectMapper) {
        this.functionName = functionName;
        this.lambdaClient = lambdaClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public String extractText(TextExtractInput inputData) {
        try {
            InvokeRequest request = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(SdkBytes.fromUtf8String(objectMapper.writeValueAsString(inputData)))
                    .build();
            return lambdaClient.invoke(request).payload().asUtf8String();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TextExtractProcessException(TEXT_EXTRACT_PROCESS_ERROR_MESSAGE);
        }
    }
}
