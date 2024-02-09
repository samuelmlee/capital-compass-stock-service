package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.dto.TickerSubscriptionMessageDTO;
import org.capitalcompass.stockservice.handler.WebSocketSessionManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing client subscriptions to ticker updates.
 * This service maintains a record of clients and their subscribed tickers,
 * and updates these subscriptions as needed. It communicates with the
 * WebSocketSessionManager to send subscription messages over the WebSocket
 * connection.
 */
@Service
@RequiredArgsConstructor
public class TickerSubscriptionService {

    /**
     * A map holding client IDs and their associated set of subscribed ticker symbols.
     */
    private final Map<String, Set<String>> clientSubscriptions = new HashMap<>();

    /**
     * The WebSocketSessionManager used to send subscription messages to the WebSocket server.
     */
    private final WebSocketSessionManager webSocketSessionManager;

    /**
     * Updates the subscription list for a specific client based on the provided DTO.
     *
     * @param messageDTO The data transfer object containing the user ID and a list of symbols
     *                   the client wants to subscribe to.
     * @return A Mono<Void> indicating the completion of the subscription update process.
     */
    public Mono<Void> updateClientSubscriptions(TickerSubscriptionMessageDTO messageDTO) {
        clientSubscriptions.put(messageDTO.getUserId(), new HashSet<>(messageDTO.getSymbols()));
        return sendSubscribeMessage();
    }

    /**
     * Removes all subscriptions for a specific client based on their client ID and sends an unsubscribe message.
     *
     * @param clientId The ID of the client whose subscriptions are to be removed.
     * @return A Mono<Void> indicating the completion of the removal process.
     */
    public Mono<Void> removeClientSubscriptions(String clientId) {
        Set<String> subscriptionsRemoved = clientSubscriptions.remove(clientId);
        return sendUnsubscribeMessage(subscriptionsRemoved);
    }

    /**
     * Sends a consolidated list of all current subscriptions to the WebSocket server
     * to ensure the server is only sending updates for subscribed tickers.
     *
     * @return A Mono<Void> indicating the completion of the send process.
     */
    private Mono<Void> sendSubscribeMessage() {
        Set<String> currentSubscriptions = getAllSubscriptions();
        return webSocketSessionManager.sendSubscriptionMessage(currentSubscriptions, "subscribe");
    }

    /**
     * Sends an unsubscribe message for the specified ticker symbols.
     * <p>
     * Filters out any ticker symbols that are not currently subscribed to before sending the unsubscribe message.
     * This ensures that only relevant subscriptions are considered for unsubscription.
     *
     * @param subscriptionsRemoved A set of ticker symbols to unsubscribe from.
     * @return A Mono<Void> signaling completion when the unsubscribe message is sent, or an error if sending fails.
     */
    private Mono<Void> sendUnsubscribeMessage(Set<String> subscriptionsRemoved) {
        Set<String> otherSubscriptions = getAllSubscriptions();
        Set<String> unsubscribedSubscriptions = subscriptionsRemoved.stream().filter(sub -> !otherSubscriptions.contains(sub)).collect(Collectors.toSet());

        return webSocketSessionManager.sendSubscriptionMessage(unsubscribedSubscriptions, "unsubscribe");
    }

    /**
     * Aggregates all subscriptions from all clients into a single set of ticker symbols.
     *
     * @return A set containing all unique ticker symbols that any client has subscribed to.
     */
    private Set<String> getAllSubscriptions() {
        Collection<Set<String>> allSubscriptions = clientSubscriptions.values();
        return allSubscriptions.stream().flatMap(Collection::stream).collect(Collectors.toSet());

    }
}
