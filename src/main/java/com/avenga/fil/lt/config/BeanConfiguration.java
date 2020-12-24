package com.avenga.fil.lt.config;

import com.avenga.fil.lt.config.properties.SupportedValues;
import com.avenga.fil.lt.config.properties.TranslationOption;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties({SupportedValues.class, TranslationOption.class})
public class BeanConfiguration {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().build();
    }
}
