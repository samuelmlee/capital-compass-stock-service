package org.capitalcompass.stockservice.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.TickerSnapshot;
import org.capitalcompass.stockservice.dto.TickerSnapshotMapDTO;
import org.capitalcompass.stockservice.dto.TickerSymbolDTO;
import org.capitalcompass.stockservice.service.MarketDataService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stocks/market/snapshot/tickers")
public class MarketDataController {

    private final MarketDataService marketDataService;

    @GetMapping("/{symbolDTO}")
    public Mono<TickerSnapshot> getTickerSnapshot(@PathVariable @Valid TickerSymbolDTO symbolDTO) {
        return marketDataService.getTickerSnapshot(symbolDTO.getSymbol());
    }

    @GetMapping("/map")
    public Mono<TickerSnapshotMapDTO> getTickerSnapshotMap(@RequestParam Set<String> symbols) {
        return marketDataService.getTickerSnapshotMap(symbols);
    }
}
