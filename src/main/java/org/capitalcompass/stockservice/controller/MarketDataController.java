package org.capitalcompass.stockservice.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.TickerSnapshot;
import org.capitalcompass.stockservice.dto.TickerSnapshotMapDTO;
import org.capitalcompass.stockservice.dto.TickerSymbolDTO;
import org.capitalcompass.stockservice.service.MarketDataService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Set;

/**
 * REST controller for market data related operations.
 * This controller handles HTTP requests for retrieving ticker snapshots.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stocks/market/snapshot/tickers")
public class MarketDataController {

    private final MarketDataService marketDataService;

    /**
     * Retrieves a snapshot of the specified ticker.
     *
     * @param symbolDTO A DTO containing the ticker symbol.
     * @return A Mono of TickerSnapshot containing the snapshot data for the specified symbol.
     * @throws javax.validation.ConstraintViolationException if the provided symbolDTO is not valid.
     */
    @GetMapping("/{symbolDTO}")
    public Mono<TickerSnapshot> getTickerSnapshot(@PathVariable @Valid TickerSymbolDTO symbolDTO) {
        return marketDataService.getTickerSnapshot(symbolDTO.getSymbol());
    }

    /**
     * Retrieves a map of ticker snapshots for the given set of symbols.
     *
     * @param symbols A set of ticker symbols to retrieve the snapshot map for.
     * @return A Mono of TickerSnapshotMapDTO containing a map of ticker symbols strings to their snapshots.
     */
    @GetMapping("/map")
    public Mono<TickerSnapshotMapDTO> getTickerSnapshotMap(@RequestParam Set<String> symbols) {
        return marketDataService.getTickerSnapshotMap(symbols);
    }
}
