package org.capitalcompass.capitalcompassstocks.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.service.MarketDataService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
@RequiredArgsConstructor
public class MarketDataController {

    private final MarketDataService marketDataService;

    public Mono<ServerResponse> getTickerSnapshot(ServerRequest request) {
        String tickerSymbol = request.pathVariable("ticker");
        return marketDataService.getTickerSnapshot(tickerSymbol).flatMap(snapshotDTO -> ok()
                .contentType(MediaType.APPLICATION_JSON).bodyValue(snapshotDTO));
    }
}
