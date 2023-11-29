package com.etta.edtech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfig {
    @Value("${aws.cognito.dev.clientId}")
    private String clientId;
    @Value("${aws.cognito.dev.secretKey}")
    private String secretKey;
    @Value("${aws.cognito.dev.accessKey}")
    private String accessKey;

    @Value("${aws.cognito.dev.userPoolId}")
    private String userPoolId;

    @Bean
    public String getSecretKey() {return secretKey;}

    @Bean
    public String getAccessKey() {return accessKey;}

    @Bean
    public String getClientId() {return clientId;}

    @Bean
    public String getUserPoolId() {return userPoolId;}


}
