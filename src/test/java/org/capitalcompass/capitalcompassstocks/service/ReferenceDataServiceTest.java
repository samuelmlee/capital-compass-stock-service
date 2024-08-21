package org.capitalcompass.capitalcompassstocks.service;


import io.r2dbc.spi.R2dbcBadGrammarException;
import org.capitalcompass.stockservice.api.*;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.dto.TickerDetailDTO;
import org.capitalcompass.stockservice.dto.TickerTypesDTO;
import org.capitalcompass.stockservice.dto.TickersDTO;
import org.capitalcompass.stockservice.dto.TickersSearchConfigDTO;
import org.capitalcompass.stockservice.entity.TickerDetail;
import org.capitalcompass.stockservice.exception.PolygonClientErrorException;
import org.capitalcompass.stockservice.exception.TickerDetailRepositoryException;
import org.capitalcompass.stockservice.exception.TickerNotFoundException;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.capitalcompass.stockservice.service.ReferenceDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void getTickersByConfigWithCursorOK() {
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
    public void getTickersByConfigNoCursorOK() {
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
    public void getTickersByConfigNoResultsOK() {

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
    public void getTickersByConfigError() {
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
    public void getTickersByCursorWithCursorOK() {
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
    public void getTickersByCursorNoCursorOK() {
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
    public void getTickersByCursorError() {
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
    void getTickerDetailFromRepoOK() {
        String tickerSymbol = "TSLA";

        TickerDetail mockDetail = TickerDetail.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

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
    public void getTickerDetailFromRepoError() {
        String tickerSymbol = "TSLA";

        when(referenceDataClient.getTickerDetails(tickerSymbol)).thenReturn(Mono.just(TickerDetailResponse.builder().build()));

        when(tickerDetailRepository.findBySymbol(tickerSymbol)).thenReturn(Mono.error(new R2dbcBadGrammarException()));

        Mono<TickerDetailDTO> result = referenceDataService.getTickerDetail(tickerSymbol);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof TickerDetailRepositoryException
                )
                .verify();
    }

    @Test
    void getTickerDetailFromClientOK() {
        String tickerSymbol = "TSLA";

        TickerDetail mockDetail = TickerDetail.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        TickerDetailResult mockResult = TickerDetailResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        TickerDetailResponse mockResponse = TickerDetailResponse.builder()
                .results(mockResult)
                .build();

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
    void getTickerDetailFromClientNoResult() {
        String tickerSymbol = "TSLA";

        TickerDetailResponse mockResponse = TickerDetailResponse.builder()
                .results(null)
                .build();

        when(referenceDataClient.getTickerDetails(tickerSymbol)).thenReturn(Mono.just(mockResponse));

        when(tickerDetailRepository.findBySymbol(tickerSymbol)).thenReturn(Mono.empty());

        Mono<TickerDetailDTO> result = referenceDataService.getTickerDetail(tickerSymbol);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof TickerNotFoundException
                )
                .verify();
    }

    @Test
    public void getTickerDetailFromClientError() {
        String tickerSymbol = "TSLA";

        when(referenceDataClient.getTickerDetails(tickerSymbol)).thenReturn(Mono.just(TickerDetailResponse.builder().build()));

        when(tickerDetailRepository.findBySymbol(tickerSymbol)).thenReturn(Mono.error(new R2dbcBadGrammarException()));

        Mono<TickerDetailDTO> result = referenceDataService.getTickerDetail(tickerSymbol);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof TickerDetailRepositoryException
                )
                .verify();
    }

    @Test
    void getTickerTypesOK() {
        TickerTypeResult mockResult1 = new TickerTypeResult("CS", "Common Stock");
        TickerTypeResult mockResult2 = new TickerTypeResult("PFD", "Preferred Stock");


        TickerTypesResponse mockResponse = TickerTypesResponse.builder()
                .results(List.of(mockResult1, mockResult2))
                .count(2)
                .build();


        when(referenceDataClient.getTickerTypes()).thenReturn(Mono.just(mockResponse));

        Mono<TickerTypesDTO> result = referenceDataService.getTickerTypes();
        StepVerifier.create(result)
                .consumeNextWith(tickerTypesDTO -> {
                    assertEquals(tickerTypesDTO.getResults().size(), 2);
                    assertTrue(tickerTypesDTO.getResults().contains(mockResult1));
                })
                .verifyComplete();
    }

    @Test
    void getTickerTypesError() {
        when(referenceDataClient.getTickerTypes()).thenReturn(Mono.error(new PolygonClientErrorException("Server Error")));

        Mono<TickerTypesDTO> result = referenceDataService.getTickerTypes();

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PolygonClientErrorException
                )
                .verify();
    }

    @Test
    void registerTickersRepoFoundOK() {
        Set<String> tickerSymbol = Set.of("TSLA");

        TickerDetail mockDetailTSLA = TickerDetail.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        when(referenceDataClient.getTickerDetails(anyString())).thenReturn(Mono.just(TickerDetailResponse.builder().build()));

        when(tickerDetailRepository.findBySymbol("TSLA")).thenReturn(Mono.just(mockDetailTSLA));

        Mono<Set<String>> result = referenceDataService.registerTickers(tickerSymbol);

        StepVerifier.create(result)
                .consumeNextWith(registeredSymbols -> {
                    assertEquals(registeredSymbols, tickerSymbol);
                })
                .verifyComplete();
    }

    @Test
    void registerTickersRepositoryFindError() {
        Set<String> tickerSymbols = Set.of("TSLA");

        when(tickerDetailRepository.findBySymbol("TSLA")).thenReturn(Mono.error(new R2dbcBadGrammarException()));
        when(referenceDataClient.getTickerDetails("TSLA")).thenReturn(Mono.just(TickerDetailResponse.builder().build()));

        Mono<Set<String>> result = referenceDataService.registerTickers(tickerSymbols);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof TickerDetailRepositoryException
                )
                .verify();
    }

    @Test
    void registerTickersClientFoundOK() {
        Set<String> tickerSymbol = Set.of("TSLA");

        TickerDetail mockDetailTSLA = TickerDetail.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        TickerDetailResult mockDetailResultTSLA = TickerDetailResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();


        when(tickerDetailRepository.findBySymbol("TSLA")).thenReturn(Mono.empty());

        when(tickerDetailRepository.save(any(TickerDetail.class))).thenReturn(Mono.just(mockDetailTSLA));

        when(referenceDataClient.getTickerDetails(anyString())).thenReturn(Mono.just(TickerDetailResponse.builder().results(mockDetailResultTSLA).build()));

        Mono<Set<String>> result = referenceDataService.registerTickers(tickerSymbol);

        StepVerifier.create(result)
                .consumeNextWith(registeredSymbols -> {
                    assertEquals(registeredSymbols, tickerSymbol);
                })
                .verifyComplete();
    }

    @Test
    void registerTickersClientFoundSaveError() {
        Set<String> tickerSymbols = Set.of("TSLA");

        TickerDetailResult mockDetailResultTSLA = TickerDetailResult.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        when(tickerDetailRepository.findBySymbol("TSLA")).thenReturn(Mono.empty());

        when(tickerDetailRepository.save(any(TickerDetail.class))).thenReturn(Mono.error(new R2dbcBadGrammarException()));

        when(referenceDataClient.getTickerDetails(anyString())).thenReturn(Mono.just(TickerDetailResponse.builder().results(mockDetailResultTSLA).build()));

        Mono<Set<String>> result = referenceDataService.registerTickers(tickerSymbols);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof TickerDetailRepositoryException
                )
                .verify();
    }

    @Test
    void registerTickersPartialRepoClientOK() {
        Set<String> tickerSymbols = Set.of("TSLA", "AAPL");

        TickerDetailResult mockDetailResultAAPL = TickerDetailResult.builder()
                .symbol("AAPL")
                .name("Apple Inc.")
                .market("stocks")
                .primaryExchange("XNAS")
                .type("CS")
                .build();

        TickerDetail mockDetailAAPL = TickerDetail.builder()
                .symbol("AAPL")
                .name("Apple Inc.")
                .market("stocks")
                .primaryExchange("XNAS")
                .type("CS")
                .build();

        TickerDetail mockDetailTSLA = TickerDetail.builder()
                .symbol("TSLA")
                .name("Tesla, Inc.")
                .market("stocks")
                .primaryExchange("NASDAQ")
                .type("Equity")
                .build();

        when(tickerDetailRepository.findBySymbol("TSLA")).thenReturn(Mono.just(mockDetailTSLA));
        when(referenceDataClient.getTickerDetails("TSLA")).thenReturn(Mono.empty());

        when(tickerDetailRepository.findBySymbol("AAPL")).thenReturn(Mono.empty());
        when(referenceDataClient.getTickerDetails("AAPL")).thenReturn(Mono.just(TickerDetailResponse.builder().results(mockDetailResultAAPL).build()));

        when(tickerDetailRepository.save(any(TickerDetail.class))).thenReturn(Mono.just(mockDetailAAPL));

        Mono<Set<String>> result = referenceDataService.registerTickers
                (tickerSymbols);


        StepVerifier.create(result)
                .assertNext(registeredSymbols -> {
                    assertEquals(2, registeredSymbols.size());
                    assertTrue(registeredSymbols.containsAll(tickerSymbols));
                })
                .verifyComplete();
    }
}