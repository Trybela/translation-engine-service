package com.avenga.fil.lt.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "supported")
public class SupportedValues {

    private Set<String> extensions;

    private Set<String> contentTypes;
}
