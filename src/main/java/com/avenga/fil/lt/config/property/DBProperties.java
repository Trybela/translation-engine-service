package com.avenga.fil.lt.config.property;

import com.avenga.fil.lt.constant.Profiles;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties(prefix = "db")
@Profile(Profiles.DB)
@Getter
@Setter
public class DBProperties {

    private String dbSecretName;
    private String driverClassName;
}
