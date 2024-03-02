package org.capitalcompass.stockservice.repository;

import org.capitalcompass.stockservice.entity.TickerMarketData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerMarketDataRepository extends ReactiveCrudRepository<TickerMarketData, Long> {

//    Flux<TickerMarketData> saveAll(List<TickerMarketData> tickerMarketDataList);
}
