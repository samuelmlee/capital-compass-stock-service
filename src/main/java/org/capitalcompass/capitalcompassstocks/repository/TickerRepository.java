package org.capitalcompass.capitalcompassstocks.repository;

import org.capitalcompass.capitalcompassstocks.entity.Ticker;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerRepository extends ReactiveCrudRepository<Ticker, Long> {
}
