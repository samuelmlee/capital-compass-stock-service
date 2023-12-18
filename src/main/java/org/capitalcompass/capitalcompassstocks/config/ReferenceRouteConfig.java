package org.capitalcompass.capitalcompassstocks.config;

import org.capitalcompass.capitalcompassstocks.controller.ReferenceDataController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReferenceRouteConfig {

    @Bean
    RouterFunction<ServerResponse> referenceRoutes(ReferenceDataController referenceDataController) {
        String TICKERS_REFERENCE_PATH = "/v1/stocks/reference/tickers";
        return route()
                .path(TICKERS_REFERENCE_PATH, builder -> builder
                        .nest(accept(MediaType.APPLICATION_JSON), nestedBuilder -> nestedBuilder
                                .GET("/types", referenceDataController::getTickerTypes)
                                .GET("/details/{ticker}", referenceDataController::getTickerDetails)
                                .GET("/cursor/{cursor}", referenceDataController::getTickersByCursor))
                        .GET(referenceDataController::getTickersByParams)
                        .POST(referenceDataController::registerTicker)
                )
                .build();

    }
}
