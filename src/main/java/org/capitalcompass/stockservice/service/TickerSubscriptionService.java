package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.dto.TickerSubscriptionMessageDTO;
import org.capitalcompass.stockservice.handler.WebSocketSessionManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TickerSubscriptionService {

    private final Map<String, Set<String>> clientSubscriptions = new HashMap<>();

    private final WebSocketSessionManager webSocketSessionManager;

    public Mono<Void> updateClientSubscriptions(TickerSubscriptionMessageDTO messageDTO) {
        clientSubscriptions.put(messageDTO.getUserId(), new HashSet<>(messageDTO.getSymbols()));
        return sendSubscribeMessage();
    }

    public Mono<Void> removeClientSubscriptions(String clientId) {
        clientSubscriptions.remove(clientId);
        return sendSubscribeMessage();
    }

    private Mono<Void> sendSubscribeMessage() {
        Set<String> currentSubscriptions = getAllSubscriptions(clientSubscriptions);
        return webSocketSessionManager.sendSubscribeMessage(currentSubscriptions);
    }


    private Set<String> getAllSubscriptions(Map<String, Set<String>> subscriptions) {
        Collection<Set<String>> allSubscriptions = subscriptions.values();
        return allSubscriptions.stream().flatMap(Collection::stream).collect(Collectors.toSet());

    }
}
