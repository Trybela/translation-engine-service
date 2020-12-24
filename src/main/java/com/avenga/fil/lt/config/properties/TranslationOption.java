package com.avenga.fil.lt.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "translate")
public class TranslationOption {

    private TranslateOptionEnum option;

    @Getter
    @RequiredArgsConstructor
    public enum TranslateOptionEnum {
        AWS("AWS"), AZURE("AZURE");

        private final String value;
    }
}
