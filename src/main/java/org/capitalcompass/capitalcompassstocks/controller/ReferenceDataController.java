package org.capitalcompass.capitalcompassstocks.controller;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.dto.*;
import org.capitalcompass.capitalcompassstocks.service.ReferenceDataService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/v1/stocks/reference/tickers")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    @GetMapping
    public Mono<TickersDTO> getTickersByParams(@Valid TickersSearchConfigDTO config) {
        return referenceDataService.getTickers(config);
    }

    @GetMapping("/details/{ticker}")
    public Mono<TickerDetailDTO> getTickerDetails(@PathVariable(name = "ticker") String symbol) {
        return referenceDataService.getTickerDetail(symbol);
    }

    @PostMapping("/register")
    public Mono<Set<String>> registerTickers(@Valid @RequestBody TickerRequestDTO tickerRequest) {
        return referenceDataService.registerTickers(tickerRequest.getSymbols());
    }

    @GetMapping("/cursor/{cursor}")
    public Mono<TickersDTO> getTickersByCursor(@PathVariable(name = "cursor") String cursor) {
        return referenceDataService.getTickersByCursor(cursor);
    }

    @GetMapping("/types")
    public Mono<TickerTypesDTO> getTickerTypes() {
        return referenceDataService.getTickerTypes();
    }
}
