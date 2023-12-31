package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.TickerSnapshot;
import org.capitalcompass.stockservice.client.MarketDataClient;
import org.capitalcompass.stockservice.dto.TickerSnapshotDTO;
import org.capitalcompass.stockservice.dto.TickerSnapshotMapDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketDataClient marketDataClient;

    private final ReferenceDataService referenceDataService;

    public Mono<TickerSnapshot> getTickerSnapshot(String tickerSymbol) {
        return marketDataClient.getTickerSnapShot(tickerSymbol).flatMap(response -> Mono.just(response.getTicker()));
    }

    public Flux<TickerSnapshot> getAllTickerSnapshots() {
        return marketDataClient.getAllTickerSnapShots();
    }

    public Mono<TickerSnapshotMapDTO> getTickerSnapshotMap(Set<String> tickerSymbols) {
        return getAllTickerSnapshots()
                .filter(tickerSnapshot -> tickerSymbols.contains(tickerSnapshot.getSymbol()))
                .flatMap(this::buildTickerSnapshotDTO)
                .collectMap(TickerSnapshotDTO::getSymbol)
                .flatMap(map -> Mono.just(TickerSnapshotMapDTO.builder()
                        .tickers(map)
                        .build()));
    }

    private Mono<TickerSnapshotDTO> buildTickerSnapshotDTO(TickerSnapshot tickerSnapshot) {
        return referenceDataService.getTickerDetail(tickerSnapshot.getSymbol()).flatMap(detail -> {
            TickerSnapshotDTO dto = TickerSnapshotDTO.builder()
                    .updated(tickerSnapshot.getUpdated())
                    .symbol(tickerSnapshot.getSymbol())
                    .name(detail.getResult().getName())
                    .day(tickerSnapshot.getDay())
                    .prevDay(tickerSnapshot.getPrevDay())
                    .build();

            return Mono.just(dto);
        });
    }
}
