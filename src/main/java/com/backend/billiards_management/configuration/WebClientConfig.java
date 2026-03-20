package com.backend.billiards_management.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "forecast-client")
@Data
public class WebClientConfig {

    private String baseUrl;

    @Bean
    public WebClient.Builder webClient() {
        return WebClient.builder().baseUrl(baseUrl);
    }
}
