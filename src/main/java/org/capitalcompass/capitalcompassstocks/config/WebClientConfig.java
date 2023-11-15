package org.capitalcompass.capitalcompassstocks.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${api.polygon.secret}")
    private String polygonSecret;

    @Value("${api.polygon.base-url}")
    private String polygonUrl;


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(polygonUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, polygonSecret)
                .build();

    }
}
