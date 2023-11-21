package org.capitalcompass.capitalcompassstocks.controller;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.TickersSearchConfig;
import org.capitalcompass.capitalcompassstocks.service.TickersService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
@RequiredArgsConstructor
public class TickersController {

    private final TickersService tickersService;
    
    public Mono<ServerResponse> getTickers(ServerRequest request) {
        TickersSearchConfig config = fromQueryParamsToSearchConfig(request.queryParams());
        return tickersService.getTickers(config).flatMap(responseDTO -> ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDTO)
        );
    }

    public Mono<ServerResponse> getTickersByCursor(ServerRequest request) {
        String cursor = request.pathVariable("cursor");
        TickersSearchConfig config = TickersSearchConfig.builder().cursor(cursor).build();
        return tickersService.getTickers(config).flatMap(responseDTO -> ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDTO)
        );
    }

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
