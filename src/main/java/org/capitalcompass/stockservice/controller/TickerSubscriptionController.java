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

/**
 * Controller for handling ticker price subscription requests.
 * This class is responsible for managing subscriptions to ticker updates,
 * including registering new subscriptions and dispatching ticker updates to subscribed clients.
 */
@Log4j2
@Controller
@RequiredArgsConstructor
public class TickerSubscriptionController {

    private final TickerSubscriptionService tickerSubscriptionService;

    private final TickerMessageBroker tickerMessageBroker;

    /**
     * Subscribes to ticker updates based on the provided subscription messages.
     * For each subscription message received, this method updates client subscriptions
     * and subscribes to the ticker updates from the message broker.
     *
     * @param dtoFlux A {@link Flux} stream of {@link TickerSubscriptionMessageDTO} objects representing
     *                subscription messages from clients.
     * @return A {@link Flux} stream of {@link TickerMessage} objects containing the ticker updates for subscribed tickers.
     */
    @MessageMapping("ticker-sub")
    public Flux<TickerMessage> subscribeToTickers(Flux<TickerSubscriptionMessageDTO> dtoFlux) {
        return dtoFlux.flatMap(messageDTO -> {
            log.debug("Subscription Message: " + messageDTO);
            return tickerSubscriptionService.updateClientSubscriptions(messageDTO)
                    .thenMany(tickerMessageBroker.subscribe());
        });
    }
}
