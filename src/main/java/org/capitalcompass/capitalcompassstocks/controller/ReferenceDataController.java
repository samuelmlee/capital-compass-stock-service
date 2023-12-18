package org.capitalcompass.capitalcompassstocks.controller;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.dto.TickerRequestDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersSearchConfigDTO;
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

        return referenceDataService.getTickers(config).flatMap(tickersDTO -> ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tickersDTO)
        );
    }

    public Mono<ServerResponse> getTickerDetails(ServerRequest request) {
        String tickerSymbol = request.pathVariable("ticker");
        return referenceDataService.getTickerDetails(tickerSymbol).flatMap(detailsDTO -> ok()
                .contentType(MediaType.APPLICATION_JSON).bodyValue(detailsDTO));

    }

    public Mono<ServerResponse> registerTicker(ServerRequest request) {
        Mono<TickerRequestDTO> tickerRequestMono = request.bodyToMono(TickerRequestDTO.class);

        return tickerRequestMono.flatMap(tickerRequestDTO ->
                referenceDataService.registerTickers(tickerRequestDTO.getSymbols()).flatMap(validated -> ok()
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(validated)));
    }

    public Mono<ServerResponse> getTickersByCursor(ServerRequest request) {
        String cursor = request.pathVariable("cursor");
        return referenceDataService.getTickersByCursor(cursor).flatMap(responseDTO -> ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDTO)
        );
    }

    public Mono<ServerResponse> getTickerTypes(ServerRequest request) {
        return referenceDataService.getTickerTypes().flatMap(responseDTO -> ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDTO));
    }

    private TickersSearchConfigDTO fromQueryParamsToSearchConfig(MultiValueMap<String, String> queryParams) {
        return TickersSearchConfigDTO.builder()
                .symbol(queryParams.getFirst("ticker"))
                .searchTerm(queryParams.getFirst("search-term"))
                .type(queryParams.getFirst("type"))
                .build();
    }
}
