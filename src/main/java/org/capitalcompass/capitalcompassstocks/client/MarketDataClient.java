package org.capitalcompass.capitalcompassstocks.client;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.AllTickerSnapshotsResponse;
import org.capitalcompass.capitalcompassstocks.model.TickerSnapShotResponse;
import org.capitalcompass.capitalcompassstocks.model.TickerSnapshot;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
                .retrieve().bodyToMono(TickerSnapShotResponse.class);
    }

    public Flux<TickerSnapshot> getAllTickerSnapShots() {
        return webClient.get().uri(snapshotsUri + "/tickers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(AllTickerSnapshotsResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getTickers()));
    }


}
