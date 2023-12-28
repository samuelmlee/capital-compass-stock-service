package org.capitalcompass.capitalcompassstocks.service;

import org.capitalcompass.capitalcompassstocks.api.*;
import org.capitalcompass.capitalcompassstocks.client.ReferenceDataClient;
import org.capitalcompass.capitalcompassstocks.dto.TickerDetailDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersSearchConfigDTO;
import org.capitalcompass.capitalcompassstocks.entity.TickerDetail;
import org.capitalcompass.capitalcompassstocks.exception.PolygonClientErrorException;
import org.capitalcompass.capitalcompassstocks.repository.TickerDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReferenceDataServiceTest {

    @Mock
    private ReferenceDataClient referenceDataClient;

    @Mock
    private TickerDetailRepository tickerDetailRepository;

    @InjectMocks
    private ReferenceDataService referenceDataService;

    @Test
    public void getTickersOKNoCursorTest() {
        TickersSearchConfigDTO mockConfig = TickersSearchConfigDTO.builder()
                .searchTerm("TESLA")
                .build();
        TickerResult teslaResult = TickersResultHelper.createTickerResult("TSLA", "Tesla, Inc. Common Stock", "stocks", "usd", "XNAS");
        TickersResponse mockResponse = TickersResponseHelper.createTickersResponse(List.of(teslaResult), 1, null);

        when(referenceDataClient.getTickers(mockConfig)).thenReturn(Mono.just(mockResponse));

        Mono<TickersDTO> result = referenceDataService.getTickers(mockConfig);

        StepVerifier.create(result)
                .consumeNextWith(tickersDTO -> {
                            assertEquals(tickersDTO.getResults(), mockResponse.getResults());
                            assertEquals(tickersDTO.getNextCursor(), "");
                        }
                )
                .verifyComplete();
    }

    @Test
    public void getTickersErrorTest() {
        TickersSearchConfigDTO mockConfig = TickersSearchConfigDTO.builder().build();
        PolygonClientErrorException mockException = new PolygonClientErrorException("Bad Request");

        when(referenceDataClient.getTickers(mockConfig)).thenReturn(Mono.error(mockException));

        Mono<TickersDTO> result = referenceDataService.getTickers(mockConfig);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PolygonClientErrorException
                )
                .verify();
    }

    @Test
    public void getTickersByCursorOkTest() {
        String cursor = "YWN0aXZlPXRyd1";
        String nextCursor = "YWN0aXZlPXRyd2";

        TickerResult teslaResult = TickersResultHelper.createTickerResult("TSLA", "Tesla, Inc. Common Stock", "stocks", "usd", "XNAS");
        TickersResponse mockResponse = TickersResponseHelper.createTickersResponse(List.of(teslaResult), 1, "https://api.polygon.io/v3/reference/tickers?cursor=" + nextCursor);

        when(referenceDataClient.getTickersByCursor(cursor)).thenReturn(Mono.just(mockResponse));

        Mono<TickersDTO> result = referenceDataService.getTickersByCursor(cursor);

        StepVerifier.create(result)
                .consumeNextWith(tickersDTO -> {
                    assertEquals(tickersDTO.getNextCursor(), nextCursor);
                    assertEquals(tickersDTO.getResults(), mockResponse.getResults());
                })
                .verifyComplete();
    }

    @Test
    public void getTickersByCursorErrorTest() {
        String cursor = "YWN0aXZlPXRyd1";
        PolygonClientErrorException mockException = new PolygonClientErrorException("Bad Request");

        when(referenceDataClient.getTickersByCursor(cursor)).thenReturn(Mono.error(mockException));

        Mono<TickersDTO> result = referenceDataService.getTickersByCursor(cursor);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PolygonClientErrorException
                )
                .verify();
    }

    @Test
    void getTickerDetailTest() {
        String tickerSymbol = "TSLA";
        TickerDetailResult mockDetailResult = TickerDetailResultHelper.createTickerDetailResult();
        TickerDetailResponse mockDetailResponse = TickerDetailResponseHelper.createTickerDetailResponse(mockDetailResult);
        TickerDetail mockDetail = TickerDetailResultHelper.buildTickerDetailFromResult(mockDetailResult);

        when(referenceDataClient.getTickerDetails(tickerSymbol)).thenReturn(Mono.just(mockDetailResponse));
        when(tickerDetailRepository.findBySymbol(tickerSymbol)).thenReturn(Mono.empty());
        when(tickerDetailRepository.save(mockDetail)).thenReturn(Mono.just(mockDetail));

        Mono<TickerDetailDTO> result = referenceDataService.getTickerDetail(tickerSymbol);

        StepVerifier.create(result)
                .consumeNextWith(tickerDetailDTO -> {
                    assertEquals(tickerDetailDTO.getResult(), mockDetail);
                })
                .verifyComplete();
    }
}