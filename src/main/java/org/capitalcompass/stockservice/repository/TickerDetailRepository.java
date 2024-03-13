package org.capitalcompass.stockservice.repository;

import org.capitalcompass.stockservice.entity.TickerDetail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TickerDetailRepository extends ReactiveCrudRepository<TickerDetail, Long> {

    Mono<TickerDetail> findBySymbol(String tickerSymbol);

}
