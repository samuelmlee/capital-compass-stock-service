package org.capitalcompass.stockservice.client;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.AllTickerSnapshotsResponse;
import org.capitalcompass.stockservice.api.TickerSnapShotResponse;
import org.capitalcompass.stockservice.api.TickerSnapshot;
import org.capitalcompass.stockservice.exception.PolygonClientErrorException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Client class for fetching market data from polygon.io,
 * retrieving ticker snapshots and related information.
 */
@Component
@RequiredArgsConstructor
public class MarketDataClient {

    private final WebClient webClient;

    private final String snapshotsUri = "/v2/snapshot/locale/us/markets/stocks";

    /**
     * Retrieves a single ticker snapshot for the given symbol.
     *
     * @param tickerSymbol The symbol of the ticker to retrieve.
     * @return A Mono of TickerSnapShotResponse containing the snapshot data.
     * @throws PolygonClientErrorException if there is a WebClientResponseException or any other network error.
     */
    public Mono<TickerSnapShotResponse> getTickerSnapShot(String tickerSymbol) {
        return webClient.get().uri(snapshotsUri + "/tickers/{symbol}", tickerSymbol)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerSnapShotResponse.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new PolygonClientErrorException("WebClientResponseException occurred getting Ticker Snapshot : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new PolygonClientErrorException("A network error occurred getting Ticker Snapshot: " + ex.getMessage()))
                );
    }

    /**
     * Retrieves snapshots for a set of ticker symbols.
     *
     * @param tickerSymbols The set of ticker symbols to retrieve snapshots for.
     * @return A Flux of TickerSnapshot containing the snapshot data for each symbol.
     * @throws PolygonClientErrorException if there is a WebClientResponseException or any other network error.
     */
    public Flux<TickerSnapshot> getTickerSnapShots(Set<String> tickerSymbols) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path(snapshotsUri + "/tickers")
                        .queryParam("tickers", String.join(",", tickerSymbols))
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(AllTickerSnapshotsResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getTickers()))
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new PolygonClientErrorException("WebClientResponseException occurred getting All Ticker Snapshots : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new PolygonClientErrorException("A network error occurred getting All Ticker Snapshots: " + ex.getMessage()))
                );
    }


}
