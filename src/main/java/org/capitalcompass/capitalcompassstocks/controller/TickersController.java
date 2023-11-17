package org.capitalcompass.capitalcompassstocks.controller;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.TickersSearchConfig;
import org.capitalcompass.capitalcompassstocks.service.TickersService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
@RequiredArgsConstructor
public class TickersController {

    private final TickersService tickersService;

    private final String TICKERS_PATH = "/stocks/tickers";

    @Bean
    public RouterFunction<ServerResponse> getTickers() {
        return route(GET(TICKERS_PATH), request -> {
            TickersSearchConfig config = fromQueryParamsToSearchConfig(request.queryParams());
            return tickersService.getTickers(config).flatMap(responseDTO -> ok().contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(responseDTO)
            );
        });
    }

    private TickersSearchConfig fromQueryParamsToSearchConfig(MultiValueMap<String, String> queryParams) {
        return TickersSearchConfig.builder()
                .cursor(queryParams.getFirst("cursor"))
                .tickerSymbol(queryParams.getFirst("ticker-symbol"))
                .searchTerm(queryParams.getFirst("search-term"))
                .resultsCount(Integer.getInteger(queryParams.getFirst("results-count")))
                .active(Boolean.getBoolean(queryParams.getFirst("active")))
                .build();
    }


}
