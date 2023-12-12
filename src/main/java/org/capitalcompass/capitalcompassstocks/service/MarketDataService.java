package org.capitalcompass.capitalcompassstocks.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.client.MarketDataClient;
import org.capitalcompass.capitalcompassstocks.model.TickerSnapshotDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketDataClient marketDataClient;

    public Mono<TickerSnapshotDTO> getTickerSnapshot(String tickerSymbol) {
        return marketDataClient.getTickerSnapShot(tickerSymbol).flatMap(response -> {
            TickerSnapshotDTO dto = TickerSnapshotDTO.builder()
                    .ticker(response.getTicker())
                    .build();
            return Mono.just(dto);
        });
    }
}
