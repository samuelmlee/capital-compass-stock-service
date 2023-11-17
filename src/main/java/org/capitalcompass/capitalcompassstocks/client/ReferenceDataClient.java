package org.capitalcompass.capitalcompassstocks.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.TickersResponse;
import org.capitalcompass.capitalcompassstocks.model.TickersSearchConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ReferenceDataClient {

    private final WebClient webClient;

    private final String tickersUri = "/v3/reference/tickers";

    public Mono<TickersResponse> getTickers(TickersSearchConfig config) {

        return webClient.get().uri(uri ->
                        uri.path(tickersUri)
                                .queryParam("active", config.getActive())
                                .queryParam("cursor", config.getCursor())
                                .queryParam("search", config.getSearchTerm())
                                .queryParam("limit", config.getResultsCount())
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickersResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class,
                        exception -> Mono.empty());
    }

}
