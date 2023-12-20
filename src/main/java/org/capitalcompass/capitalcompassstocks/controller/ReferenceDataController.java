package org.capitalcompass.capitalcompassstocks.controller;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.dto.*;
import org.capitalcompass.capitalcompassstocks.service.ReferenceDataService;
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
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    private final Validator validator;

    public Mono<ServerResponse> getTickersByParams(ServerRequest request) {
        TickersSearchConfigDTO config = fromQueryParamsToSearchConfig(request.queryParams());
        Set<ConstraintViolation<TickersSearchConfigDTO>> violations = validator.validate(config);
        if (!violations.isEmpty()) {
            return ServerResponse.badRequest().bodyValue(violations);
        }

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(referenceDataService.getTickers(config), TickersDTO.class);
    }

    public Mono<ServerResponse> getTickerDetails(ServerRequest request) {
        String tickerSymbol = request.pathVariable("ticker");
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(referenceDataService.getTickerDetail(tickerSymbol), TickerDetailDTO.class);

    }

    public Mono<ServerResponse> registerTickers(ServerRequest request) {
        Mono<TickerRequestDTO> tickerRequestMono = request.bodyToMono(TickerRequestDTO.class);

        return tickerRequestMono.flatMap(tickerRequestDTO -> ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(referenceDataService.registerTickers(tickerRequestDTO.getSymbols()), Set.class));
    }

    public Mono<ServerResponse> getTickersByCursor(ServerRequest request) {
        String cursor = request.pathVariable("cursor");
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(referenceDataService.getTickersByCursor(cursor), TickersDTO.class);
    }

    public Mono<ServerResponse> getTickerTypes(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(referenceDataService.getTickerTypes(), TickerTypesDTO.class);
    }

    private TickersSearchConfigDTO fromQueryParamsToSearchConfig(MultiValueMap<String, String> queryParams) {
        return TickersSearchConfigDTO.builder()
                .symbol(queryParams.getFirst("ticker"))
                .searchTerm(queryParams.getFirst("search-term"))
                .type(queryParams.getFirst("type"))
                .build();
    }
}
