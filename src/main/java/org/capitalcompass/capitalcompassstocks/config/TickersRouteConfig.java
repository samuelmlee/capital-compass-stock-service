package org.capitalcompass.capitalcompassstocks.config;

import org.capitalcompass.capitalcompassstocks.controller.TickersController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TickersRouteConfig {

    @Bean
    RouterFunction<ServerResponse> routes(TickersController tickersController) {
        String TICKERS_PATH = "/stocks/tickers";
        return route().path(TICKERS_PATH, builder1 -> builder1
                        .nest(accept(MediaType.APPLICATION_JSON), builder2 -> builder2
                                .GET(tickersController::getTickers)
                                .GET("/cursor/{cursor}", tickersController::getTickers)))
                .build();

    }
}
