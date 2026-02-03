package com.example.orderservice.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: SecurityProperties class
 */


@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
