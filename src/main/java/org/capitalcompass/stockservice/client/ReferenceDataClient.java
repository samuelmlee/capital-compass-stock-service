package org.capitalcompass.stockservice.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.TickerDetailResponse;
import org.capitalcompass.stockservice.api.TickerTypesResponse;
import org.capitalcompass.stockservice.api.TickersResponse;
import org.capitalcompass.stockservice.dto.TickersSearchConfigDTO;
import org.capitalcompass.stockservice.exception.PolygonClientErrorException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReferenceDataClient {

    private final WebClient webClient;
    private final String tickersUri = "/v3/reference/tickers";

    public Mono<TickersResponse> getTickers(TickersSearchConfigDTO config) {
        return webClient.get().uri(uri ->
                        uri.path(tickersUri)
                                .queryParam("search", config.getSearchTerm())
                                .queryParam("type", config.getType())
                                .queryParam("ticker", config.getSymbol())
                                .queryParam("active", true)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TickersResponse.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new PolygonClientErrorException("WebClientResponseException occurred getting Tickers : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new PolygonClientErrorException("A network error occurred getting Tickers: " + ex.getMessage()))
                );
    }

    public Mono<TickerDetailResponse> getTickerDetails(String tickerSymbol) {
        return webClient.get().uri(uri ->
                        uri.path(tickersUri)
                                .path("/" + tickerSymbol)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerDetailResponse.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new PolygonClientErrorException("WebClientResponseException occurred getting Ticker Details : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new PolygonClientErrorException("A network error occurred getting Ticker Details: " + ex.getMessage()))
                );
    }

    public Mono<TickersResponse> getTickersByCursor(String cursor) {
        return webClient.get().uri(uri ->
                        uri.path(tickersUri)
                                .queryParam("cursor", cursor)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickersResponse.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new PolygonClientErrorException("WebClientResponseException occurred getting Ticker with Cursor : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new PolygonClientErrorException("A network error occurred getting Ticker with Cursor: " + ex.getMessage()))
                );
    }

    public Mono<TickerTypesResponse> getTickerTypes() {
        return webClient.get().uri(uri ->
                        uri.path(tickersUri + "/types")
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerTypesResponse.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new PolygonClientErrorException("WebClientResponseException occurred getting Ticker Types : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new PolygonClientErrorException("A network error occurred getting Ticker Types: " + ex.getMessage()))
                );
    }

}


