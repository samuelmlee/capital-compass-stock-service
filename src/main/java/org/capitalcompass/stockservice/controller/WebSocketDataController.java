package org.capitalcompass.stockservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.SubscriptionMessage;
import org.capitalcompass.stockservice.client.WebSocketDataHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Log4j2
@Controller
@RequiredArgsConstructor
public class WebSocketDataController {

    private final WebSocketDataHandler webSocketDataHandler;

    @MessageMapping("/ticker-sub")
    public Mono<Void> message(SubscriptionMessage message) {
        log.debug("SubscriptionMessage: " + message);
        return webSocketDataHandler.subscribeToChannels(message);
    }
}
