package org.capitalcompass.capitalcompassstocks.config;

import org.capitalcompass.capitalcompassstocks.controller.MarketDataController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MarketRouteConfig {

    @Bean
    RouterFunction<ServerResponse> marketRoutes(MarketDataController marketDataController) {
        String TICKERS_MARKET_PATH = "/v1/stocks/market";
        return route()
                .path(TICKERS_MARKET_PATH, builder -> builder
                        .nest(accept(MediaType.APPLICATION_JSON), nestedBuilder -> nestedBuilder
                                .GET("/snapshot/tickers/{ticker}", marketDataController::getTickerSnapshot))
                )
                .build();

    }
}
