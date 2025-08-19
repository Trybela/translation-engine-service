package com.avenga.fil.lt.util;

import com.avenga.fil.lt.config.property.DBProperties;
import com.avenga.fil.lt.config.property.SecretManagerDBProperties;
import com.avenga.fil.lt.constant.Profiles;
import com.avenga.fil.lt.exception.SecretManagerApiCallException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

@Service
@Profile(Profiles.DB)
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SecretManagerService {

    private final DBProperties dbProperties;
    private final ObjectMapper objectMapper;
    private final SecretsManagerClient secretsManagerClient;

    public SecretManagerDBProperties getSecretManagerDBProperties() {
        return retrieveDBSecret(objectMapper, secretsManagerClient, dbProperties.getDbSecretName());
    }

    private SecretManagerDBProperties retrieveDBSecret(ObjectMapper objectMapper,
                                                       SecretsManagerClient secretsManagerClient,
                                                       String dbSecretName) {
        try {
            var secretValueRequest = GetSecretValueRequest.builder().secretId(dbSecretName).build();
            var secretResponse = secretsManagerClient.getSecretValue(secretValueRequest);
            return objectMapper.readValue(secretResponse.secretString(), SecretManagerDBProperties.class);
        } catch (Exception ex) {
            throw new SecretManagerApiCallException(ex.getMessage());
        }
    }
}
