package org.capitalcompass.stockservice.repository;

import org.capitalcompass.stockservice.entity.TickerMarketData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TickerMarketDataRepository extends ReactiveCrudRepository<TickerMarketData, Long> {
    
    @Query("DELETE t1 FROM ticker_market_data t1 INNER JOIN ticker_market_data t2 WHERE t1.id < t2.id AND t1.ticker_detail_id = t2.ticker_detail_id;")
    Mono<Void> deleteDuplicateTickerDetail();
}
