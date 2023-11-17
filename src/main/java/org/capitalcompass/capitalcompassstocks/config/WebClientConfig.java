package org.capitalcompass.capitalcompassstocks.config;

import org.capitalcompass.capitalcompassstocks.exception.PolygonClientErrorException;
import org.capitalcompass.capitalcompassstocks.exception.PolygonServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Value("${api.polygon.secret}")
    private String polygonSecret;

    @Value("${api.polygon.base-url}")
    private String polygonUrl;

    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class).flatMap(errorBody ->
                        Mono.error(new PolygonServerErrorException(errorBody)));
            }
            if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class).flatMap(errorBody ->
                        Mono.error(new PolygonClientErrorException(errorBody)));
            }
            return Mono.just(clientResponse);
        });
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(polygonUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, polygonSecret)
                .filter(errorHandler())
                .build();
    }

}
