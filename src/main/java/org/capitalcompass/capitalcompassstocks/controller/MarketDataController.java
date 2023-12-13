package org.capitalcompass.capitalcompassstocks.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.TickerSnapshotDTO;
import org.capitalcompass.capitalcompassstocks.service.MarketDataService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
@RequiredArgsConstructor
public class MarketDataController {

    private final String TICKER_SNAPSHOT_URL = "/v1/stocks/market/snapshot/tickers";

    private final MarketDataService marketDataService;

    @Bean
    public RouterFunction<ServerResponse> getTickerSnapshot() {
        return route(GET(TICKER_SNAPSHOT_URL + "/{symbol}"), request -> {
            String tickerSymbol = request.pathVariable("symbol");
            return marketDataService.getTickerSnapshot(tickerSymbol).flatMap(snapshotDTO -> ok()
                    .contentType(MediaType.APPLICATION_JSON).bodyValue(snapshotDTO));
        });
    }

    @Bean
    public RouterFunction<ServerResponse> getAllTickerSnapshots() {
        return route(GET(TICKER_SNAPSHOT_URL), request ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(marketDataService.getAllTickerSnapshots(), TickerSnapshotDTO.class));
    }
}
