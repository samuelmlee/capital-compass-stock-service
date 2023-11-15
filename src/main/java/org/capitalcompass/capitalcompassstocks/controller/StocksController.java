package org.capitalcompass.capitalcompassstocks.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class StocksController {

    @GetMapping("/stocks/status")
    public Mono<String> getStatus() {
        return Mono.just("stocks ms ok");
    }
}

