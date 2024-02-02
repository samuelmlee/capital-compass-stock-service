package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.handler.ControlMessageSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TickerSubscriptionService {


    private final Map<String, Set<String>> clientSubscriptions = new HashMap<>();

    private final ControlMessageSender controlMessageSender;

    public Mono<Void> updateClientSubscriptions(String message, String clientId) {
        clientSubscriptions.putIfAbsent(clientId, Set.of(message));
//        String channels = message.getSymbols().stream().map(symbol -> "AM." + symbol).collect(Collectors.joining(","));
//        return controlMessageSender.sendSubscribeMessage(channels);
        return Mono.empty();
    }

    public Mono<Void> removeClientSubscriptions(String clientId) {
        clientSubscriptions.remove(clientId);
        return Mono.empty();
    }
}
