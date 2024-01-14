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

/**
 * Service class for handling market data operations.
 * This class provides methods to retrieve ticker snapshots and their details.
 */
@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketDataClient marketDataClient;

    private final ReferenceDataService referenceDataService;

    /**
     * Retrieves a single ticker snapshot for the given symbol.
     *
     * @param tickerSymbol The symbol of the ticker to retrieve.
     * @return A Mono of TickerSnapshot containing the snapshot data.
     */
    public Mono<TickerSnapshot> getTickerSnapshot(String tickerSymbol) {
        return marketDataClient.getTickerSnapShot(tickerSymbol).flatMap(response -> Mono.just(response.getTicker()));
    }

    /**
     * Retrieves snapshots for a set of ticker symbols.
     *
     * @param tickerSymbols The set of ticker symbols to retrieve snapshots for.
     * @return A Flux of TickerSnapshot containing the snapshots data for each symbol.
     */
    public Flux<TickerSnapshot> getTickerSnapshots(Set<String> tickerSymbols) {
        return marketDataClient.getTickerSnapShots(tickerSymbols);
    }

    /**
     * Retrieves a map of ticker snapshots for a given set of ticker symbols.
     *
     * @param tickerSymbols The set of ticker symbols to retrieve the snapshot
     *                      map for.
     * @return A Mono of TickerSnapshotMapDTO containing a map of ticker symbols to their snapshots.
     */
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

    /**
     * Retrieves a map of available ticker snapshots for a given set of ticker symbols.
     *
     * @param tickerSymbols The set of ticker symbols.
     * @return A Mono of a Map where the key is the ticker symbol and the value is the TickerSnapshotDTO.
     */
    private Mono<Map<String, TickerSnapshotDTO>> getAvailableSnapshotMap(Set<String> tickerSymbols) {
        return getTickerSnapshots(tickerSymbols)
                .flatMap(this::buildTickerSnapshotDTO).collectMap(TickerSnapshotDTO::getSymbol);
    }

    /**
     * Builds a TickerSnapshotDTO from a TickerSnapshot.
     *
     * @param tickerSnapshot The TickerSnapshot to convert to a TickerSnapshotDTO.
     * @return A Mono of TickerSnapshotDTO containing the details from the given TickerSnapshot.
     */
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

    /**
     * Builds a default TickerSnapshotDTO for a given symbol when no snapshot is available.
     *
     * @param symbol The symbol for which to build the default snapshot.
     * @return A Mono of TickerSnapshotDTO with default values set.
     */
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
