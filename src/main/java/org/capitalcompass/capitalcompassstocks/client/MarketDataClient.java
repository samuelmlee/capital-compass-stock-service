package org.capitalcompass.capitalcompassstocks.client;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.AggregatesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MarketDataClient {

    private final WebClient webClient;

    public Mono<AggregatesResponse> getAggregates() {
        return Mono.empty();
    }


}
