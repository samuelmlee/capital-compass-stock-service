package org.capitalcompass.capitalcompassstocks.service;

import org.capitalcompass.stockservice.api.TickerDetailResponse;
import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.entity.TickerDetail;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.capitalcompass.stockservice.repository.TickerMarketDataRepository;
import org.capitalcompass.stockservice.service.MarketDataUpdateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketDataUpdateServiceTest {

    @Mock
    private ReferenceDataClient referenceDataClient;

    @Mock
    private TickerDetailRepository tickerDetailRepository;

    @Mock
    private TickerMarketDataRepository tickerMarketDataRepository;

    @InjectMocks
    private MarketDataUpdateService marketDataUpdateService;

    @Test
    void saveLatestTickerMarketDataOK() {
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

        when(tickerDetailRepository.findAll()).thenReturn(Flux.just(mockDetail));
        when(referenceDataClient.getTickerDetails(anyString())).thenReturn(Mono.just(mockResponse));
        when(tickerMarketDataRepository.saveAll(any(Iterable.class))).thenReturn(Flux.empty());


        marketDataUpdateService.saveLatestTickerMarketData();

        verify(tickerDetailRepository, times(1)).findAll();
        verify(referenceDataClient, times(1)).getTickerDetails(anyString());
        verify(tickerMarketDataRepository, times(1)).saveAll(any(Iterable.class));
    }

}
