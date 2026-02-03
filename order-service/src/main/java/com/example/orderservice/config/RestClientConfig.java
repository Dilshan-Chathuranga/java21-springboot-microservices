package com.example.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */


@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        // ⏱ TIMEOUTS — THIS IS IMPORTANT
        factory.setConnectTimeout(3_000);
        factory.setReadTimeout(3_000);

        return new RestTemplate(factory);
    }
}
