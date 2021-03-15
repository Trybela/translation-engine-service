package com.avenga.fil.lt.config;

import com.avenga.fil.lt.config.property.DBProperties;
import com.avenga.fil.lt.config.property.SecretManagerDBProperties;
import com.avenga.fil.lt.constant.Profiles;
import com.avenga.fil.lt.util.SecretManagerService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile(Profiles.DB)
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource(final SecretManagerService secretManagerService, final DBProperties dbProperties) {
        var secretManagerDBProperties = secretManagerService.getSecretManagerDBProperties();
        return DataSourceBuilder.create()
                .url(constructUrl(secretManagerDBProperties))
                .username(dbProperties.getDbSecretName())
                .driverClassName(dbProperties.getDriverClassName())
                .build();
    }

    public String constructUrl(SecretManagerDBProperties secretManagerDBProperties) {
        return "jdbc-secretsmanager:postgresql://" + secretManagerDBProperties.getHost() + ":"
                + secretManagerDBProperties.getPort() + "/" + secretManagerDBProperties.getDbname();
    }
}
