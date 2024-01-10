package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.TickerSnapshot;
import org.capitalcompass.stockservice.client.MarketDataClient;
import org.capitalcompass.stockservice.dto.TickerSnapshotDTO;
import org.capitalcompass.stockservice.dto.TickerSnapshotMapDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketDataClient marketDataClient;

    private final ReferenceDataService referenceDataService;

    public Mono<TickerSnapshot> getTickerSnapshot(String tickerSymbol) {
        return marketDataClient.getTickerSnapShot(tickerSymbol).flatMap(response -> Mono.just(response.getTicker()));
    }

    public Flux<TickerSnapshot> getTickerSnapshots(Set<String> tickerSymbols) {
        return marketDataClient.getTickerSnapShots(tickerSymbols);
    }

    public Mono<TickerSnapshotMapDTO> getTickerSnapshotMap(Set<String> tickerSymbols) {
        Mono<Map<String, TickerSnapshotDTO>> availableSnapshotMap = getAvailableSnapshotMap(tickerSymbols);

        return availableSnapshotMap.flatMap(availableMap ->
                Flux.fromIterable(tickerSymbols)
                        .flatMap(symbol -> Mono.justOrEmpty(availableMap.get(symbol))
                                .switchIfEmpty(buildDefaultSnapshotDTO(symbol)))
                        .collectMap(TickerSnapshotDTO::getSymbol)
                        .map(completeMap -> TickerSnapshotMapDTO.builder().tickers(completeMap).build())
        );
    }

    private Mono<Map<String, TickerSnapshotDTO>> getAvailableSnapshotMap(Set<String> tickerSymbols) {
        return getTickerSnapshots(tickerSymbols)
                .flatMap(this::buildTickerSnapshotDTO).collectMap(TickerSnapshotDTO::getSymbol);
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
    private Mono<TickerSnapshotDTO> buildDefaultSnapshotDTO(String symbol) {
        return referenceDataService.getTickerDetail(symbol).flatMap(detail -> {
            TickerSnapshotDTO dto = TickerSnapshotDTO.builder()
                    .updated(null)
                    .symbol(symbol)
                    .name(detail.getResult().getName())
                    .day(null)
                    .prevDay(null)
                    .build();
            return Mono.just(dto);
        });
    }
}
