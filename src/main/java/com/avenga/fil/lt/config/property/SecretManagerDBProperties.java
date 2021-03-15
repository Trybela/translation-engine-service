package com.avenga.fil.lt.config.property;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretManagerDBProperties {

    private String host;
    private int port;
    private String dbname;
}
