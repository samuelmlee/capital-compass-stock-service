package org.capitalcompass.capitalcompassstocks.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.client.MarketDataClient;
import org.capitalcompass.capitalcompassstocks.model.TickerSnapshot;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketDataClient marketDataClient;

    public Mono<TickerSnapshot> getTickerSnapshot(String tickerSymbol) {
        return marketDataClient.getTickerSnapShot(tickerSymbol).flatMap(response -> Mono.just(response.getTicker()));
    }

    public Flux<TickerSnapshot> getAllTickerSnapshots() {
        return marketDataClient.getAllTickerSnapShots();

    }
}
