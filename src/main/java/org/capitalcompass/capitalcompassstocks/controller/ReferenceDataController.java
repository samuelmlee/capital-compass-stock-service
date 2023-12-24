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

    @GetMapping("/cursor/{cursor}")
    public Mono<TickersDTO> getTickersByCursor(@PathVariable(name = "cursor") String cursor) {
        return referenceDataService.getTickersByCursor(cursor);
    }

    @GetMapping("/details/{symbol}")
    public Mono<TickerDetailDTO> getTickerDetails(@Valid TickerSymbolDTO symbolDto) {
        return referenceDataService.getTickerDetail(symbolDto.getSymbol());
    }

    @PostMapping("/register")
    public Mono<Set<String>> registerTickers(@RequestBody @Valid TickerRequestDTO tickerRequest) {
        return referenceDataService.registerTickers(tickerRequest.getSymbols());
    }
    
    @GetMapping("/types")
    public Mono<TickerTypesDTO> getTickerTypes() {
        return referenceDataService.getTickerTypes();
    }
}
