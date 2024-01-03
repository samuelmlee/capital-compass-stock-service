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

@Component
@RequiredArgsConstructor
public class MarketDataClient {

    private final WebClient webClient;

    private final String snapshotsUri = "/v2/snapshot/locale/us/markets/stocks";

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

    public Flux<TickerSnapshot> getAllTickerSnapShots() {
        return webClient.get().uri(snapshotsUri + "/tickers")
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
