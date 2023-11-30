package com.etta.edtech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfig {
    @Value("${aws.cognito.dev.userClientId}")
    private String userClientId;
    @Value("${aws.cognito.dev.educatorClientId}")
    private String educatorClientId;
    @Value("${aws.cognito.dev.secretKey}")
    private String secretKey;
    @Value("${aws.cognito.dev.accessKey}")
    private String accessKey;


    @Bean
    public String getSecretKey() {return secretKey;}

    @Bean
    public String getAccessKey() {return accessKey;}

    @Bean
    public String getUserClientId() {return userClientId;}

    @Bean
    public String getEducatorClientId() {return educatorClientId;}



}
