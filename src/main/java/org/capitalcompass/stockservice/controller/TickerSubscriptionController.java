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
     * Handles subscription requests for ticker updates. Each incoming subscription request is
     * processed to update client subscriptions, and subsequently, a stream of {@link TickerMessage}
     * objects is emitted to the subscriber, providing real-time updates on tickers of interest.
     *
     * @param dtoFlux A {@link Flux}<{@link TickerSubscriptionMessageDTO}> representing a stream of
     *                subscription messages from clients. Each {@link TickerSubscriptionMessageDTO}
     *                contains details such as the client's user ID and a list of ticker symbols
     *                the client wishes to subscribe to for updates.
     * @return A {@link Flux}<{@link TickerMessage}> stream that emits ticker updates to subscribed
     * clients. Each {@link TickerMessage} contains the updated information for a specific
     * ticker symbol.
     * <p>
     * The method invokes {@link TickerSubscriptionService#updateClientSubscriptions(TickerSubscriptionMessageDTO)}
     * to update the client's subscriptions based on the received DTO. After the subscriptions are
     * updated, it subscribes to the ticker updates through {@link TickerMessageBroker#subscribe()},
     * which returns a Flux of {@link TickerMessage} to be sent back to the client.
     * <p>
     * The {@code doFinally} operator is used to perform cleanup actions when the Flux sequence
     * terminates for any reason (complete, error, or cancel). In this case, it unsubscribes the client
     * from the ticker updates by calling
     * {@link TickerSubscriptionService#removeClientSubscriptions(String)} with the client's user ID.
     */
    @MessageMapping("ticker-sub")
    public Flux<TickerMessage> subscribeToTickers(Flux<TickerSubscriptionMessageDTO> dtoFlux) {
        return dtoFlux.flatMap(messageDTO -> {
            log.debug("Subscription Message: " + messageDTO);
            return tickerSubscriptionService.updateClientSubscriptions(messageDTO)
                    .thenMany(tickerMessageBroker.subscribe())
                    .doFinally((signalType) -> tickerSubscriptionService.removeClientSubscriptions(messageDTO.getUserId()).subscribe());
        });
    }
}
