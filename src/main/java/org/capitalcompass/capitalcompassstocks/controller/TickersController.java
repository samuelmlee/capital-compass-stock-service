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

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
@RequiredArgsConstructor
public class TickersController {

    private final TickersService tickersService;

    private final Validator validator;

    public Mono<ServerResponse> getTickersByParams(ServerRequest request) {
        TickersSearchConfig config = fromQueryParamsToSearchConfig(request.queryParams());
        Set<ConstraintViolation<TickersSearchConfig>> violations = validator.validate(config);
        if (!violations.isEmpty()) {
            return ServerResponse.badRequest().bodyValue(violations);
        }

        return tickersService.getTickers(config).flatMap(responseDTO -> ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDTO)
        );
    }

    public Mono<ServerResponse> getTickerDetails(ServerRequest request) {
        String tickerSymbol = request.pathVariable("ticker");
        return tickersService.getTickerDetails(tickerSymbol).flatMap(detailsDTO -> ok()
                .contentType(MediaType.APPLICATION_JSON).bodyValue(detailsDTO));

    }

    public Mono<ServerResponse> getTickersByCursor(ServerRequest request) {
        String cursor = request.pathVariable("cursor");
        return tickersService.getTickersByCursor(cursor).flatMap(responseDTO -> ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDTO)
        );
    }

    public Mono<ServerResponse> getTickerTypes(ServerRequest request) {
        return tickersService.getTickerTypes().flatMap(responseDTO -> ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDTO));
    }

    private TickersSearchConfig fromQueryParamsToSearchConfig(MultiValueMap<String, String> queryParams) {
        return TickersSearchConfig.builder()
                .ticker(queryParams.getFirst("ticker"))
                .searchTerm(queryParams.getFirst("search-term"))
                .type(queryParams.getFirst("type"))
                .build();
    }
}
