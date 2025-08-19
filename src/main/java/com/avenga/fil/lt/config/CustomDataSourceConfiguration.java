package com.avenga.fil.lt.config;

import com.avenga.fil.lt.constant.Profiles;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Profiles.NOT_DB)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class CustomDataSourceConfiguration {
}
