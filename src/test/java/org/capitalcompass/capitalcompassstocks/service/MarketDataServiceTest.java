package org.capitalcompass.capitalcompassstocks.service;

import org.capitalcompass.stockservice.api.DailyBar;
import org.capitalcompass.stockservice.api.TickerSnapShotResponse;
import org.capitalcompass.stockservice.api.TickerSnapshot;
import org.capitalcompass.stockservice.client.MarketDataClient;
import org.capitalcompass.stockservice.dto.TickerSnapshotDTO;
import org.capitalcompass.stockservice.exception.PolygonClientErrorException;
import org.capitalcompass.stockservice.service.MarketDataService;
import org.capitalcompass.stockservice.service.ReferenceDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarketDataServiceTest {

    @Mock
    private MarketDataClient marketDataClient;

    @Mock
    private ReferenceDataService referenceDataService;

    @InjectMocks
    private MarketDataService marketDataService;

    @Test
    public void getTickerSnapshotOK() {
        String tickerSymbol = "AAPL";

        TickerSnapshot mockTickerSnapshot = TickerSnapshot.builder()
                .updated(1706317200000000000L)
                .symbol("AAPL")
                .day(DailyBar.builder()
                        .closePrice(192)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(194)
                        .build())
                .build();

        TickerSnapShotResponse response = TickerSnapShotResponse.builder()
                .ticker(mockTickerSnapshot)
                .status("OK")
                .build();

        when(marketDataClient.getTickerSnapShot(anyString())).thenReturn(Mono.just(response));

        Mono<TickerSnapshot> responseMono = marketDataService.getTickerSnapshot(tickerSymbol);

        StepVerifier.create(responseMono).consumeNextWith(tickerSnapshot -> {
                    assertEquals(tickerSnapshot, mockTickerSnapshot);
                })
                .verifyComplete();
    }

    @Test
    public void getTickerSnapshotError() {
        String tickerSymbol = "AAPL";

        PolygonClientErrorException mockException = new PolygonClientErrorException("Bad Request");

        when(marketDataClient.getTickerSnapShot(anyString())).thenReturn(Mono.error(mockException));

        Mono<TickerSnapshot> responseMono = marketDataService.getTickerSnapshot(tickerSymbol);

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof PolygonClientErrorException
                )
                .verify();
    }

    @Test
    public void getTickerSnapshotsTest() {
        Set<String> tickerSymbols = Set.of("AAPL", "MSFT");

        TickerSnapshot mockAppleTickerSnapshot = TickerSnapshot.builder()
                .updated(1706317200000000000L)
                .symbol("AAPL")
                .day(DailyBar.builder()
                        .closePrice(192)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(194)
                        .build())
                .build();

        TickerSnapshot mockMicrosoftTickerSnapshot = TickerSnapshot.builder()
                .updated(1706317200000000000L)
                .symbol("MSFT")
                .day(DailyBar.builder()
                        .closePrice(403)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(404)
                        .build())
                .build();

        List<TickerSnapshot> mockTickerSnapshots = List.of(mockAppleTickerSnapshot, mockMicrosoftTickerSnapshot);

        when(marketDataClient.getTickerSnapShots(anySet())).thenReturn(Flux.fromIterable(mockTickerSnapshots));

        StepVerifier.create(marketDataService.getTickerSnapshots(tickerSymbols))
                .expectNextSequence(mockTickerSnapshots)
                .verifyComplete();
    }

    @Test
    public void getTickerSnapshotsError() {
        Set<String> tickerSymbols = Set.of("AAPL", "MSFT");

        PolygonClientErrorException mockException = new PolygonClientErrorException("Internal Server Error");

        when(marketDataClient.getTickerSnapShots(anySet())).thenReturn(Flux.error(mockException));

        Flux<TickerSnapshot> responseFlux = marketDataService.getTickerSnapshots(tickerSymbols);

        StepVerifier.create(responseFlux)
                .expectErrorMatches(throwable ->
                        throwable instanceof PolygonClientErrorException
                )
                .verify();
    }

    @Test
    public void getTickerSnapshotMapOK() {
        Set<String> tickerSymbols = Set.of("AAPL", "MSFT");

        TickerSnapshot mockAppleTickerSnapshot = TickerSnapshot.builder()
                .updated(1706317200000000000L)
                .symbol("AAPL")
                .day(DailyBar.builder()
                        .closePrice(192)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(194)
                        .build())
                .build();

        TickerSnapshot mockMicrosoftTickerSnapshot = TickerSnapshot.builder()
                .updated(1706317200000000000L)
                .symbol("MSFT")
                .day(DailyBar.builder()
                        .closePrice(403)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(404)
                        .build())
                .build();

        List<TickerSnapshot> mockTickerSnapshots = List.of(mockAppleTickerSnapshot, mockMicrosoftTickerSnapshot);

        Map<String, TickerSnapshotDTO> mockTickerSnapshotMap = new HashMap<>();

        TickerSnapshotDTO mockAppleTickerSnapshotDTO = TickerSnapshotDTO.builder()
                .updated(1706317200000000000L)
                .symbol("AAPL")
                .name("Apple Inc.")
                .day(DailyBar.builder()
                        .closePrice(192)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(194)
                        .build())
                .build();

        TickerSnapshotDTO mockMicrosoftTickerSnapshotDTO = TickerSnapshotDTO.builder()
                .updated(1706317200000000000L)
                .symbol("MSFT")
                .name("Microsoft Corp")
                .day(DailyBar.builder()
                        .closePrice(403)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(404)
                        .build())
                .build();

        mockTickerSnapshotMap.put("AAPL", mockAppleTickerSnapshotDTO);
        mockTickerSnapshotMap.put("MSFT", mockMicrosoftTickerSnapshotDTO);

        when(marketDataClient.getTickerSnapShots(tickerSymbols)).thenReturn(Flux.fromIterable(mockTickerSnapshots));

        StepVerifier.create(marketDataService.getTickerSnapshotMap(tickerSymbols))
                .expectNextMatches(mapDto -> mapDto.getTickers().equals(mockTickerSnapshotMap))
                .verifyComplete();

    }
}
