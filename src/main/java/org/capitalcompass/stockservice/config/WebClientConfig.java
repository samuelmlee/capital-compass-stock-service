package org.capitalcompass.stockservice.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
@Log4j2
public class WebClientConfig {

    @Value("${polygon.api.key}")
    private String polygonSecret;

    @Value("${polygon.base.url}")
    private String polygonUrl;


    @Bean
    public WebClient webClient() {
        int bufferSize = 8 * 1024 * 1024;

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(bufferSize))
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(polygonUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + polygonSecret)
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                sb.append(clientRequest.url()).append("\n");
                clientRequest
                        .headers()
                        .forEach((name, values) -> {
                            if (!Objects.equals(name, "Authorization")) {
                                values.forEach(sb::append);
                            }
                        });
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

    ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Response: \n");
                sb.append(clientResponse.statusCode()).append("\n");
                clientResponse
                        .headers().asHttpHeaders()
                        .forEach((name, values) -> values.forEach(sb::append));
                log.debug(sb.toString());
            }
            return Mono.just(clientResponse);
        });
    }

}
