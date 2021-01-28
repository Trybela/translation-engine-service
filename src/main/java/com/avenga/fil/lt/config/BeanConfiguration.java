package com.avenga.fil.lt.config;

import com.avenga.fil.lt.config.properties.SupportedValues;
import com.avenga.fil.lt.config.properties.TranslationOption;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties({SupportedValues.class, TranslationOption.class})
public class BeanConfiguration {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().build();
    }

    @Bean
    public LambdaClient lambdaClient() {
        return LambdaClient.builder().build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }
}
