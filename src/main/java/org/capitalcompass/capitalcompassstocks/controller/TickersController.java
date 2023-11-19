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
    // TODO :
    // ADD A CONFIG REGISTERING ALL ROUTER FUNCTIONS AND A LIST OF ALL HANDLERS FOR ALL PATHS
    //        RouterFunction<ServerResponse> route = route()
//                .path("/person", builder -> builder
//                        .GET("/{id}", accept(APPLICATION_JSON), handler::getPerson)
//                        .GET(accept(APPLICATION_JSON), handler::listPeople)
//                        .POST(handler::createPerson))
//                .build();
//    https://docs.spring.io/spring-framework/reference/web/webflux-functional.html
//    https://docs.spring.io/spring-framework/reference/web/webflux-functional.html#nested-routes
    // CHECK Filtering Handler Functions nad nesting paths and handler functions


    private TickersSearchConfig fromQueryParamsToSearchConfig(MultiValueMap<String, String> queryParams) {
        String resultsCountParam = queryParams.getFirst("results-count");
        Integer resultsCount = resultsCountParam == null ? 10 : Integer.parseInt(resultsCountParam);
        String activeParam = queryParams.getFirst("active");
        Boolean active = activeParam == null || Boolean.getBoolean(activeParam);

        return TickersSearchConfig.builder()
                .cursor(queryParams.getFirst("cursor"))
                .tickerSymbol(queryParams.getFirst("ticker-symbol"))
                .searchTerm(queryParams.getFirst("search-term"))
                .resultsCount(resultsCount)
                .active(active)
                .build();
    }


}
