package org.capitalcompass.capitalcompassstocks.repository;

import org.capitalcompass.capitalcompassstocks.entity.TickerDetail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TickerDetailRepository extends ReactiveCrudRepository<TickerDetail, Long> {

    Mono<TickerDetail> findBySymbol(String tickerSymbol);
}
