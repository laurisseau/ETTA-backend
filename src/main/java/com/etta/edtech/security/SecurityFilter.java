package com.etta.edtech.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityFilter {

    private final AuthFilter authFilter;

    /**
     * This code configures a SecurityFilterChain as a Spring Bean.
     * - SecurityFilterChain defines the security configuration for HTTP requests.
     * - It disables Cross-Site Request Forgery (CSRF) protection.
     * - Specifies access control rules for URL patterns, allowing public access to "/api/user/auth/**" and
     *   requiring authentication for other requests.
     * - Configures stateless session management for the application.
     * - Specifies an AuthenticationProvider for user authentication.
     * - Adds a custom JWT authentication filter before the standard UsernamePasswordAuthenticationFilter.
     * - The configured SecurityFilterChain is returned as a Spring Bean.
     * - Exception handling is included to handle configuration errors gracefully.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
            .cors((cors) -> {
                cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.applyPermitDefaultValues();
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Add allowed methods
                    //config.addAllowedOrigin("http://localhost:3000"); // Specify your frontend URL
                    config.addAllowedOrigin("https://www.earlytech.org/");
                    return config;
                });
            })
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((req) -> req
                    .requestMatchers("/api/permitAll/**",
                            "/api/auth/**")
                    .permitAll()
                    .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/api/educator/**").hasAuthority("ROLE_EDUCATOR")
                    .requestMatchers("/api/user/**", "/api/payment/**").hasAuthority("ROLE_USER")
                    .anyRequest().authenticated()
            )
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(authFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
