package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.entity.TickerMarketData;
import org.capitalcompass.stockservice.exception.PolygonClientErrorException;
import org.capitalcompass.stockservice.exception.TickerDetailRepositoryException;
import org.capitalcompass.stockservice.exception.TickerMarketDataRepositoryException;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.capitalcompass.stockservice.repository.TickerMarketDataRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@Log4j2
@RequiredArgsConstructor
public class MarketDataUpdateService {

    private final ReferenceDataClient referenceDataClient;

    private final TickerDetailRepository tickerDetailRepository;

    private final TickerMarketDataRepository tickerMarketDataRepository;

    @Scheduled(cron = "${market-data.fetch-cron}")
    public Disposable saveLatestTickerMarketData() {
        return tickerDetailRepository.findAll()
                .onErrorResume(e -> {
                    return Mono.error(new TickerDetailRepositoryException("Error getting all ticker details "));
                })
                .flatMap(tickerDetail -> referenceDataClient.getTickerDetails(tickerDetail.getSymbol())
                        .onErrorResume(e -> {
                            return Mono.error(new PolygonClientErrorException("Error ticker details for symbol :" + tickerDetail.getSymbol()));
                        })
                        .flatMap(tickerDetailResponse -> buildTickerMarketDataFromResponse(tickerDetailResponse.getResults(), tickerDetail.getId())))
                .collectList().flatMapMany(tickerMarketDataRepository::saveAll)
                .onErrorResume(e -> {
                    return Mono.error(new TickerMarketDataRepositoryException("Error saving all ticker market data"));
                })
                .subscribe();
    }

    @Scheduled(cron = "${market-data.delete-duplicates-cron}")
    public Disposable deleteDuplicateTickerMarketData() {
        return tickerMarketDataRepository.deleteDuplicateTickerDetail()
                .onErrorResume(e -> {
                    return Mono.error(new TickerMarketDataRepositoryException("Error deleting market data duplicates"));
                })
                .subscribe();
    }

    private Mono<TickerMarketData> buildTickerMarketDataFromResponse(TickerDetailResult result, Long tickerDetailId) {
        TickerMarketData marketData = TickerMarketData.builder()
                .marketCap(result.getMarketCap())
                .shareClassSharesOutstanding(result.getShareClassSharesOutstanding())
                .weightedSharesOutstanding(result.getWeightedSharesOutstanding())
                .updatedTimestamp(Instant.now())
                .tickerDetailId(tickerDetailId)
                .build();
        return Mono.just(marketData);
    }
}
