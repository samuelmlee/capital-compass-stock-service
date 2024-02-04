package org.capitalcompass.stockservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.TickerMessage;
import org.capitalcompass.stockservice.dto.TickerSubscriptionMessageDTO;
import org.capitalcompass.stockservice.messaging.TickerMessageBroker;
import org.capitalcompass.stockservice.service.TickerSubscriptionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Log4j2
@Controller
@RequiredArgsConstructor
public class TickerSubscriptionController {

    private final TickerSubscriptionService tickerSubscriptionService;

    private final TickerMessageBroker tickerMessageBroker;

    @MessageMapping("ticker-sub")
    public Flux<TickerMessage> subscribeToTickers(TickerSubscriptionMessageDTO messageDTO) {
        log.debug("Subscription Message: " + messageDTO);
        return tickerSubscriptionService.updateClientSubscriptions(messageDTO)
                .thenMany(tickerMessageBroker.subscribe());
    }
}
