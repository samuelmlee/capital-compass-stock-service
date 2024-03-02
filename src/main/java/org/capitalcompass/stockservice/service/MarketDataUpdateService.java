package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.entity.TickerMarketData;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.capitalcompass.stockservice.repository.TickerMarketDataRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class MarketDataUpdateService {

    private final ReferenceDataClient referenceDataClient;

    private final TickerDetailRepository tickerDetailRepository;

    private final TickerMarketDataRepository tickerMarketDataRepository;

    @Scheduled(cron = "@daily")
    public Mono<Void> updateTickerMarketData() {
        return tickerDetailRepository.findAll()
                .flatMap(tickerDetail -> referenceDataClient.getTickerDetails(tickerDetail.getSymbol())
                        .flatMap(tickerDetailResponse -> buildTickerMarketDataFromResponse(tickerDetailResponse.getResults(), tickerDetail.getId())))
                .collectList().flatMapMany(tickerMarketDataList -> {
                    return tickerMarketDataRepository.saveAll(tickerMarketDataList);
                }).then();
    }

    private Mono<TickerMarketData> buildTickerMarketDataFromResponse(TickerDetailResult result, Long tickerDetailId) {
        TickerMarketData marketData = TickerMarketData.builder()
                .marketCap(result.getMarketCap())
                .shareClassSharesOutstanding(result.getShareClassSharesOutstanding())
                .weightedSharesOutstanding(result.getWeightedSharesOutstanding())
                .updatedTimestamp(new Timestamp(System.currentTimeMillis()))
                .tickerDetailId(tickerDetailId)
                .build();
        return Mono.just(marketData);
    }
}
