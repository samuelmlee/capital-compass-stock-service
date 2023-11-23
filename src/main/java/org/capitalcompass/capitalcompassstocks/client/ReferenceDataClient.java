package org.capitalcompass.capitalcompassstocks.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.TickersResponse;
import org.capitalcompass.capitalcompassstocks.model.TickersSearchConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReferenceDataClient {

    private final WebClient webClient;

    private final String tickersUri = "/v3/reference/tickers";

    public Mono<TickersResponse> getTickers(TickersSearchConfig config) {
//        return Mono.error(new PolygonServerErrorException("Polygon Server error"));
//        return Mono.error(new PolygonClientErrorException("Polygon request param invalid error"));
        return webClient.get().uri(uri ->
                        uri.path(tickersUri)
//                                .queryParam("search", config.getSearchTerm())
//                                .queryParam("type", config.getType())
                                .queryParam("active", true)
                                .queryParam("limit", 100)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickersResponse.class);
    }

    public Mono<TickersResponse> getTickersByCursor(String cursor) {
        // TODO: handling for exceeding the request limit per minute

        return webClient.get().uri(uri ->
                        uri.path(tickersUri)
                                .queryParam("cursor", cursor)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickersResponse.class);
    }

}


