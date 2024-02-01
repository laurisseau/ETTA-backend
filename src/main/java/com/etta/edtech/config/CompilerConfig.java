package com.etta.edtech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompilerConfig {

    @Value("${jdoodle.clientId}")
    private String clientId;

    @Value("${jdoodle.clientSecret}")
    private String clientSecret;

    @Bean
    public String getClientId() {return clientId;}

    @Bean
    public String getClientSecret() {return clientSecret;}


}
