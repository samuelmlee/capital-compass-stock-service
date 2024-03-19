package org.capitalcompass.capitalcompassstocks.service;

import org.capitalcompass.stockservice.api.TickerDetailResponse;
import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.entity.TickerDetail;
import org.capitalcompass.stockservice.entity.TickerMarketData;
import org.capitalcompass.stockservice.exception.PolygonClientErrorException;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.capitalcompass.stockservice.repository.TickerMarketDataRepository;
import org.capitalcompass.stockservice.service.MarketDataUpdateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class MarketDataUpdateServiceTest {

    @Captor
    ArgumentCaptor<List<TickerMarketData>> marketDataCaptor;
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
        TickerDetail mockTeslaDetail = TickerDetail.builder()
                .id(1L)
                .symbol("TSLA")
                .build();

        TickerDetail mockAppleDetail = TickerDetail.builder()
                .id(2L)
                .symbol("AAPL")
                .build();


        TickerDetailResult mockTeslaResult = TickerDetailResult.builder()
                .symbol("TSLA")
                .marketCap(10L)
                .shareClassSharesOutstanding(10L)
                .weightedSharesOutstanding(10L)
                .build();

        TickerDetailResponse mockTeslaResponse = TickerDetailResponse.builder()
                .results(mockTeslaResult)
                .build();

        TickerDetailResult mockAppleResult = TickerDetailResult.builder()
                .symbol("AAPL")
                .marketCap(20L)
                .shareClassSharesOutstanding(20L)
                .weightedSharesOutstanding(20L)
                .build();

        TickerDetailResponse mockAppleResponse = TickerDetailResponse.builder()
                .results(mockAppleResult)
                .build();


        when(tickerDetailRepository.findAll()).thenReturn(Flux.just(mockTeslaDetail, mockAppleDetail));
        when(referenceDataClient.getTickerDetails(anyString()))
                .thenReturn(Mono.just(mockTeslaResponse))
                .thenReturn(Mono.just(mockAppleResponse));
        when(tickerMarketDataRepository.saveAll(any(Iterable.class))).thenReturn(Flux.empty());

        marketDataUpdateService.saveLatestTickerMarketData();

        verify(tickerDetailRepository, times(1)).findAll();
        verify(referenceDataClient, times(2)).getTickerDetails(anyString());
        verify(tickerMarketDataRepository, times(1)).saveAll(marketDataCaptor.capture());

        List<TickerMarketData> capturedArguments = marketDataCaptor.getValue();

        TickerMarketData teslaMarketData = capturedArguments.get(0);
        assertEquals(teslaMarketData.getTickerDetailId(), 1L);
        assertEquals(teslaMarketData.getMarketCap(), 10L);

        TickerMarketData appleMarketData = capturedArguments.get(1);
        assertEquals(appleMarketData.getTickerDetailId(), 2L);
        assertEquals(appleMarketData.getMarketCap(), 20L);
    }

    @Test
    void saveLatestTickerMarketDataEmptyListOK() {

        when(tickerDetailRepository.findAll()).thenReturn(Flux.empty());

        marketDataUpdateService.saveLatestTickerMarketData();

        verify(tickerDetailRepository, times(1)).findAll();

        verify(referenceDataClient, never()).getTickerDetails(any());
        verify(tickerMarketDataRepository, never()).saveAll(any(Iterable.class));
    }

    @Test
    void saveLatestTickerMarketDataFindAllError(CapturedOutput output) {
        when(tickerDetailRepository.findAll())
                .thenReturn(Flux.error(new ConnectException("Database error")));

        marketDataUpdateService.saveLatestTickerMarketData();

        verify(tickerDetailRepository, times(1)).findAll();
        verifyNoMoreInteractions(referenceDataClient);
        verifyNoMoreInteractions(tickerMarketDataRepository);

        assertThat(output.getOut()).contains("Error fetching all Ticker Detail");
    }

    @Test
    void saveLatestTickerMarketDataFetchDetailError(CapturedOutput output) {
        TickerDetail mockTeslaDetail = TickerDetail.builder()
                .id(1L)
                .symbol("TSLA")
                .build();

        marketDataUpdateService.saveLatestTickerMarketData();

        when(tickerDetailRepository.findAll()).thenReturn(Flux.just(mockTeslaDetail));
        when(referenceDataClient.getTickerDetails(anyString()))
                .thenReturn(Mono.error(new PolygonClientErrorException("A network error occurred getting Ticker Details")));
        when(tickerMarketDataRepository.saveAll(any(Iterable.class))).thenReturn(Flux.empty());

        marketDataUpdateService.saveLatestTickerMarketData();

        verify(tickerDetailRepository, times(1)).findAll();
        verify(referenceDataClient, times(1)).getTickerDetails(anyString());
        verify(tickerMarketDataRepository, never()).saveAll(any(Iterable.class));

        assertThat(output.getOut()).contains("Error getting ticker details for symbol");
    }


}
