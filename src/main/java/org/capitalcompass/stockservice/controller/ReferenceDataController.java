package org.capitalcompass.stockservice.controller;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.dto.*;
import org.capitalcompass.stockservice.service.ReferenceDataService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

/**
 * REST controller for handling reference data operations related to tickers.
 * Provides endpoints for retrieving ticker details, ticker types, and registering ticker details.
 */
@RestController
@RequestMapping("/v1/stocks/reference/tickers")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    /**
     * Retrieves a list of tickers based on the provided search configuration.
     *
     * @param config Configuration object for searching tickers including search term, type, and symbol.
     * @return A Mono of TickersDTO containing the search results.
     */
    @GetMapping
    public Mono<TickersDTO> getTickersByParams(@Valid TickersSearchConfigDTO config) {
        return referenceDataService.getTickersByConfig(config);
    }

    /**
     * Retrieves a list of tickers based on a cursor used for pagination.
     *
     * @param cursor The cursor indicating the next result page.
     * @return A Mono of TickersDTO containing the tickers and cursor for the next page fo results.
     */
    @GetMapping("/cursor/{cursor}")
    public Mono<TickersDTO> getTickersByCursor(@PathVariable(name = "cursor") @NotBlank String cursor) {
        return referenceDataService.getTickersByCursor(cursor);
    }

    /**
     * Retrieves detailed information about a specific ticker symbol.
     *
     * @param symbolDto A DTO containing the ticker symbol.
     * @return A Mono of TickerDetailDTO containing detailed information of the specified ticker.
     * @throws javax.validation.ConstraintViolationException if the provided symbolDto is not valid.
     */
    @GetMapping("/details/{symbol}")
    public Mono<TickerDetailDTO> getTickerDetails(@Valid TickerSymbolDTO symbolDto) {
        return referenceDataService.getTickerDetail(symbolDto.getSymbol());
    }

    /**
     * Registers a set of tickers, storing their details.
     *
     * @param tickerRequest A DTO containing a set of ticker symbols to register.
     * @return A Mono of Set containing the symbols of tickers that were successfully registered.
     * @throws javax.validation.ConstraintViolationException if the provided tickerRequest is not valid.
     */
    @PostMapping("/register")
    public Mono<Set<String>> registerTickers(@RequestBody @Valid TickerRequestDTO tickerRequest) {
        return referenceDataService.registerTickers(tickerRequest.getSymbols());
    }

    /**
     * Retrieves the available types of tickers.
     *
     * @return A Mono of TickerTypesDTO containing the list of ticker types.
     */
    @GetMapping("/types")
    public Mono<TickerTypesDTO> getTickerTypes() {
        return referenceDataService.getTickerTypes();
    }

    /**
     * Retrieves the most recent news articles relating to a stock ticker symbol, including a summary of the article and a link to the original source.
     *
     * @return A Mono of TickerNewsDTO containing the list of news articles.
     */
    @GetMapping("/news")
    public Mono<TickerNewsDTO> getTickerNews(@RequestParam(required = false) String ticker) {
        return referenceDataService.getTickerNews(ticker);
    }
}
