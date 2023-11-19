package org.capitalcompass.capitalcompassstocks.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.exception.PolygonServerErrorException;
import org.capitalcompass.capitalcompassstocks.model.TickersResponse;
import org.capitalcompass.capitalcompassstocks.model.TickersSearchConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReferenceDataClient {

    private final WebClient webClient;

    private final String tickersUri = "/v3/reference/tickers";

    public Mono<TickersResponse> getTickers(TickersSearchConfig config) {
        return Mono.error(new PolygonServerErrorException("Polygon Server error"));
//        try {
//            throw new PolygonServerErrorException("Polygon Server error");
//        } catch (PolygonServerErrorException e) {
//            return Mono.error(e);
//        }
//        return Mono.error(new PolygonClientErrorException("Polygon request param invalid error"));
//        return webClient.get().uri(uri ->
//                        uri.path(tickersUri)
//                                .queryParam("active", config.getActive())
//                                .queryParam("cursor", config.getCursor())
//                                .queryParam("search", config.getSearchTerm())
//                                .queryParam("limit", config.getResultsCount())
//                                .build())
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve().bodyToMono(TickersResponse.class);
    }

}


