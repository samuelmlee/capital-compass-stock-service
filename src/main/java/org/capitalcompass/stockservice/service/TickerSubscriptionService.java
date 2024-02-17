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
    private final Map<String, Set<String>> subscriptionsPerClient = new HashMap<>();

    /**
     * A map holding ticker symbols and the associated number of users subscribed to them.
     */
    private final Map<String, Integer> subCountPerTicker = new HashMap<>();

    /**
     * The WebSocketSessionManager used to send subscription messages to the WebSocket server.
     */
    private final WebSocketSessionManager webSocketSessionManager;

    /**
     * Updates the set of ticker subscriptions for a given client based on the provided subscription message.
     * This method first retrieves the current set of subscriptions associated with the client, if any,
     * and then replaces them with the new set of subscriptions specified in the message. It also updates
     * the global subscription count for each ticker to reflect the changes.
     *
     * @param messageDTO A {@link TickerSubscriptionMessageDTO} object containing the client's user ID
     *                   and the new set of ticker symbols they wish to subscribe to.
     * @return A {@link Mono<Void>} that completes when the subscription update process is finished.
     * The update process involves updating both the client's individual subscriptions and
     * the global subscription counts for each ticker.
     */
    public Mono<Void> updateSubscriptionsPerClient(TickerSubscriptionMessageDTO messageDTO) {
        String userId = messageDTO.getUserId();
        Set<String> newSubscriptions = new HashSet<>(messageDTO.getSymbols());

        Set<String> existingSubscriptions = subscriptionsPerClient.getOrDefault(userId, Collections.emptySet());

        subscriptionsPerClient.put(userId, newSubscriptions);

        return updateSubCountPerTicker(existingSubscriptions, newSubscriptions);
    }

    /**
     * Updates the subscription count for each ticker based on the provided sets of existing and new subscriptions.
     * This method identifies tickers that need to be subscribed to or unsubscribed from by determining the difference
     * and intersection between the existing and new subscriptions. It then updates the global subscription count for
     * each ticker and initiates the sending of subscription or unsubscription messages as necessary.
     *
     * <p>This method is designed to be used internally within the service to maintain accurate subscription counts
     * and ensure that subscription messages are sent only for tickers that have actually been added or removed,
     * rather than those that remain unchanged between updates.</p>
     *
     * @param existingSubscriptions The set of ticker symbols that the client was previously subscribed to.
     * @param newSubscriptions      The set of ticker symbols that the client wants to subscribe to after the update.
     * @return A {@link Mono<Void>} that completes when all subscription and unsubscription messages have been sent.
     * This Mono represents the completion of the entire update process for subscription counts and messaging.
     */
    private Mono<Void> updateSubCountPerTicker(Set<String> existingSubscriptions, Set<String> newSubscriptions) {
        Set<String> commonSubscriptions = existingSubscriptions.stream().filter(newSubscriptions::contains).collect(Collectors.toSet());

        Set<String> symbolsToUnsubscribe = existingSubscriptions.stream().filter(sub -> !commonSubscriptions.contains(sub)).collect(Collectors.toSet());
        Set<String> symbolsToSubscribe = newSubscriptions.stream().filter(sub -> !commonSubscriptions.contains(sub)).collect(Collectors.toSet());

        Set<String> newUnsubscribed = decrementSubCountPerTicker(symbolsToUnsubscribe);
        Set<String> newSubscribed = incrementSubCountPerTicker(symbolsToSubscribe);

        return Mono.when(sendSubscribeMessage(newSubscribed), sendUnsubscribeMessage(newUnsubscribed));
    }

    /**
     * Increments the subscription count for each ticker symbol provided in the set of new subscriptions.
     * This method processes each new subscription by either initializing its count to 1 if it's not already
     * present in the global subscription count map, or incrementing its existing count by 1 if it is. It also
     * collects and returns a set of tickers that were newly subscribed to (i.e., tickers that were not previously
     * in the global subscription count map before this method was called).
     *
     * @param newSubscriptions The set of ticker symbols for which the subscription count needs to be incremented.
     *                         This represents the set of tickers newly subscribed to by a client.
     * @return A {@link Set<String>} containing the ticker symbols that were newly added to the subscription
     * count map as a result of this method call. This set includes only those tickers whose subscription
     * count was initialized to 1, indicating that they were not previously subscribed to.
     */
    private Set<String> incrementSubCountPerTicker(Set<String> newSubscriptions) {
        Set<String> newSubscribed = new HashSet<>();

        newSubscriptions.forEach(ticker -> {
            subCountPerTicker.compute(ticker, (k, v) -> {
                if (v == null) {
                    newSubscribed.add(ticker);
                    return 1;
                } else {
                    return v + 1;
                }
            });
        });
        return newSubscribed;
    }


    /**
     * Decrements the subscription count for each ticker symbol in the provided set of existing subscriptions.
     * This method processes each subscription by decrementing its count in the global subscription count map.
     * If decrementing the count results in a value of 0, it indicates that the ticker is fully unsubscribed,
     * and such tickers are collected in a set to be returned. The method assumes that the provided tickers
     * are currently subscribed (i.e., present in the subscription count map with a count of at least 1).
     *
     * @param existingSubscriptions The set of ticker symbols for which the subscription count needs to be decremented.
     *                              This represents the set of tickers that a client is unsubscribing from.
     * @return A {@link Set<String>} containing the ticker symbols that were fully unsubscribed as a result of this
     * method call. These are the tickers whose subscription count reached 0 after being decremented.
     */
    private Set<String> decrementSubCountPerTicker(Set<String> existingSubscriptions) {
        Set<String> newUnsubscribed = new HashSet<>();

        existingSubscriptions.forEach(ticker -> {
            subCountPerTicker.computeIfPresent(ticker, (k, v) -> {
                if (v == 1) {
                    newUnsubscribed.add(ticker);
                }
                return v - 1;
            });
        });
        return newUnsubscribed;
    }


    /**
     * Removes all subscriptions for a specific client based on their client ID and sends an unsubscribe message.
     *
     * @param clientId The ID of the client whose subscriptions are to be removed.
     * @return A Mono<Void> indicating the completion of the removal process.
     */
    public Mono<Void> removeClientSubscriptions(String clientId) {
        Set<String> subscriptionsRemoved = subscriptionsPerClient.remove(clientId);
        Set<String> symbolsUnsubscribed = decrementSubCountPerTicker(subscriptionsRemoved);
        return sendUnsubscribeMessage(symbolsUnsubscribed);
    }

    /**
     * Sends a consolidated list of all current subscriptions to the WebSocket server
     * to ensure the server is only sending updates for subscribed tickers.
     *
     * @return A Mono<Void> indicating the completion of the send process.
     */
    private Mono<Void> sendSubscribeMessage(Set<String> subscriptionsAdded) {
        return webSocketSessionManager.sendSubscriptionMessage(subscriptionsAdded, "subscribe");
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
        return webSocketSessionManager.sendSubscriptionMessage(subscriptionsRemoved, "unsubscribe");
    }

}
