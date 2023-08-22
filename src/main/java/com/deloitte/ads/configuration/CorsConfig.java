package com.deloitte.ads.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_HEADERS = Arrays.asList(
            "X-Requested-With", "Origin", "Content-Type", "Accept",
            "Authorization", "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers",
            "Access-Control-Allow-Methods", "Access-Control-Allow-Origin", "Access-Control-Expose-Headers",
            "Access-Control-Max-Age", "Access-Control-Request-Headers", "Access-Control-Request-Method",
            "Age", "Allow", "Alternates", "Content-Type", "Content-Range",
            "Content-Disposition", "Content-Description", "excludeloader"
    );

    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST");

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList(allowedOrigins));
        corsConfiguration.setAllowedMethods(ALLOWED_METHODS);
        corsConfiguration.setAllowedHeaders(ALLOWED_HEADERS);
        corsConfiguration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}