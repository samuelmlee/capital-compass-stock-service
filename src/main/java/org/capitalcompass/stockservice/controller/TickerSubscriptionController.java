package org.capitalcompass.stockservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.service.TickerSubscriptionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Log4j2
@Controller
@RequiredArgsConstructor
public class TickerSubscriptionController {

    private final TickerSubscriptionService tickerSubscriptionService;

    @MessageMapping("/ticker-sub")
    public Mono<Void> subscribeToTickers(String message, Principal principal) {
        log.debug("Subscription Message: " + message);
        return tickerSubscriptionService.updateClientSubscriptions(message, principal.getName());
    }
}
