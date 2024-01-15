package org.capitalcompass.capitalcompassstocks.service;


import org.capitalcompass.stockservice.api.TickerDetailResponse;
import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.api.TickerResult;
import org.capitalcompass.stockservice.api.TickersResponse;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.dto.TickerDetailDTO;
import org.capitalcompass.stockservice.dto.TickersDTO;
import org.capitalcompass.stockservice.dto.TickersSearchConfigDTO;
import org.capitalcompass.stockservice.entity.TickerDetail;
import org.capitalcompass.stockservice.exception.PolygonClientErrorException;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.capitalcompass.stockservice.service.ReferenceDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReferenceDataServiceTest {


    @Mock
    TransactionalOperator transactionalOperator;
    @Mock
    private ReferenceDataClient referenceDataClient;
    @Mock
    private TickerDetailRepository tickerDetailRepository;
    @InjectMocks
    private ReferenceDataService referenceDataService;

    @Test
    public void getTickersByConfigWithCursorOkTest() {
        String nextCursor = "YWN0aXZlPXRyd2";

        TickersSearchConfigDTO mockConfig = TickersSearchConfigDTO.builder()
                .searchTerm("TESLA")
                .build();

        TickerResult teslaResult = TickerResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc. Common Stock")
                .market("stocks")
                .currencyName("usd")
                .primaryExchange("XNAS")
                .build();
        TickersResponse mockResponse = TickersResponse.builder()
                .results(List.of(teslaResult))
                .count(1)
                .nextUrl("https://api.polygon.io/v3/reference/tickers?cursor=" + nextCursor)
                .build();

        when(referenceDataClient.getTickersByConfig(any(TickersSearchConfigDTO.class))).thenReturn(Mono.just(mockResponse));

        Mono<TickersDTO> result = referenceDataService.getTickersByConfig(mockConfig);

        StepVerifier.create(result)
                .consumeNextWith(tickersDTO -> {
                    assertEquals(tickersDTO.getNextCursor(), nextCursor);
                    assertEquals(tickersDTO.getResults(), mockResponse.getResults());
                })
                .verifyComplete();
    }

    @Test
    public void getTickersByConfigNoCursorOKTest() {
        TickersSearchConfigDTO mockConfig = TickersSearchConfigDTO.builder()
                .searchTerm("TESLA")
                .build();
        TickerResult teslaResult = TickerResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc. Common Stock")
                .market("stocks")
                .currencyName("usd")
                .primaryExchange("XNAS")
                .build();
        TickersResponse mockResponse = TickersResponse.builder()
                .results(List.of(teslaResult))
                .count(1)
                .nextUrl("")
                .build();

        when(referenceDataClient.getTickersByConfig(any(TickersSearchConfigDTO.class))).thenReturn(Mono.just(mockResponse));

        Mono<TickersDTO> result = referenceDataService.getTickersByConfig(mockConfig);

        StepVerifier.create(result)
                .consumeNextWith(tickersDTO -> {
                            assertEquals(tickersDTO.getResults(), mockResponse.getResults());
                            assertEquals(tickersDTO.getNextCursor(), "");
                        }
                )
                .verifyComplete();
    }

    @Test
    public void getTickersByConfigNoResultsOkTest() {

        TickersSearchConfigDTO mockConfig = TickersSearchConfigDTO.builder()
                .searchTerm("XYZ")
                .build();

        TickersResponse mockResponse = TickersResponse.builder()
                .results(List.of())
                .count(0)
                .nextUrl("")
                .build();

        when(referenceDataClient.getTickersByConfig(any(TickersSearchConfigDTO.class))).thenReturn(Mono.just(mockResponse));

        Mono<TickersDTO> result = referenceDataService.getTickersByConfig(mockConfig);

        StepVerifier.create(result)
                .consumeNextWith(tickersDTO -> {
                    assertEquals(tickersDTO.getNextCursor(), "");
                    assertEquals(tickersDTO.getResults(), mockResponse.getResults());
                })
                .verifyComplete();
    }

    @Test
    public void getTickersByConfigErrorTest() {
        TickersSearchConfigDTO mockConfig = TickersSearchConfigDTO.builder().build();
        PolygonClientErrorException mockException = new PolygonClientErrorException("Bad Request");

        when(referenceDataClient.getTickersByConfig(mockConfig)).thenReturn(Mono.error(mockException));

        Mono<TickersDTO> result = referenceDataService.getTickersByConfig(mockConfig);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PolygonClientErrorException
                )
                .verify();
    }

    @Test
    public void getTickersByCursorWithCursorOkTest() {
        String cursor = "YWN0aXZlPXRyd1";
        String nextCursor = "YWN0aXZlPXRyd2";

        TickerResult teslaResult = TickerResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc. Common Stock")
                .market("stocks")
                .currencyName("usd")
                .primaryExchange("XNAS")
                .build();
        TickersResponse mockResponse = TickersResponse.builder()
                .results(List.of(teslaResult))
                .count(1)
                .nextUrl("https://api.polygon.io/v3/reference/tickers?cursor=" + nextCursor)
                .build();

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
    public void getTickersByCursorNoCursorOkTest() {
        String cursor = "YWN0aXZlPXRyd2";

        TickerResult teslaResult = TickerResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc. Common Stock")
                .market("stocks")
                .currencyName("usd")
                .primaryExchange("XNAS")
                .build();
        TickersResponse mockResponse = TickersResponse.builder()
                .results(List.of(teslaResult))
                .count(1)
                .nextUrl("")
                .build();

        when(referenceDataClient.getTickersByCursor(cursor)).thenReturn(Mono.just(mockResponse));

        Mono<TickersDTO> result = referenceDataService.getTickersByCursor(cursor);

        StepVerifier.create(result)
                .consumeNextWith(tickersDTO -> {
                    assertEquals(tickersDTO.getNextCursor(), "");
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
    void getTickerDetailFromRepoOKTest() {
        String tickerSymbol = "TSLA";

        TickerDetail mockDetail = TickerDetail.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("Tesla, Inc.")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer((Answer<Mono<?>>) invocation -> {
                    Mono<?> mono = invocation.getArgument(0);
                    return Mono.from(mono);
                });

        when(referenceDataClient.getTickerDetails(tickerSymbol)).thenReturn(Mono.just(TickerDetailResponse.builder().build()));

        when(tickerDetailRepository.findBySymbol(tickerSymbol)).thenReturn(Mono.just(mockDetail));

        Mono<TickerDetailDTO> result = referenceDataService.getTickerDetail(tickerSymbol);
        StepVerifier.create(result)
                .consumeNextWith(tickerDetailDTO -> {
                    assertEquals(tickerDetailDTO.getResult(), mockDetail);
                })
                .verifyComplete();
    }

    @Test
    public void getTickerDetailFromRepoErrorTest() {
    }

    @Test
    void getTickerDetailFromClientOKTest() {
        String tickerSymbol = "TSLA";

        TickerDetail mockDetail = TickerDetail.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("Tesla, Inc.")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        TickerDetailResult mockResult = TickerDetailResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("Tesla, Inc.")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        TickerDetailResponse mockResponse = TickerDetailResponse.builder()
                .results(mockResult)
                .build();

        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer((Answer<Mono<?>>) invocation -> {
                    Mono<?> mono = invocation.getArgument(0);
                    return Mono.from(mono);
                });

        when(referenceDataClient.getTickerDetails(tickerSymbol)).thenReturn(Mono.just(mockResponse));

        when(tickerDetailRepository.findBySymbol(tickerSymbol)).thenReturn(Mono.empty());

        Mono<TickerDetailDTO> result = referenceDataService.getTickerDetail(tickerSymbol);
        StepVerifier.create(result)
                .consumeNextWith(tickerDetailDTO -> {
                    assertEquals(tickerDetailDTO.getResult(), mockDetail);
                })
                .verifyComplete();
    }

    @Test
    void getTickerDetailFromClientNoResultTest() {
    }

    @Test
    public void getTickerDetailFromClientErrorTest() {
    }
}