package com.example.paymentservice.security;

import com.example.paymentservice.config.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: SecurityConfig class
 */

@Configuration
public class SecurityConfig {

    private final SecurityProperties properties;

    public SecurityConfig(SecurityProperties properties) {
        this.properties = properties;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterBefore(
                new ApiKeyAuthFilter(properties.getApiKey()),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
