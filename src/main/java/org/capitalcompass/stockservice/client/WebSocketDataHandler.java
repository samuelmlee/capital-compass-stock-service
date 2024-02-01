package org.capitalcompass.stockservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.ActionMessage;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.api.SubscriptionMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketDataHandler implements WebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${polygon.api.key}")
    private String polygonSecret;
    private WebSocketSession currentSession;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        this.currentSession = session;
        return session.receive().map(WebSocketMessage::getPayloadAsText).log()
                .flatMap(messageString -> {
                    List<PolygonMessage> messages = mapToPolygonMessages(messageString);

                    switch (messages.get(0).getEvent()) {
                        case "status":
                            return processStatusMessage(messages);
                        case "AM":
                            return processTickerMessage(messages);
                        default:
                            return Mono.empty();
                    }
                }).then();
    }

    public Mono<Void> subscribeToChannels(SubscriptionMessage message) {
        String channels = message.getSymbols().stream().map(symbol -> "AM." + symbol).collect(Collectors.joining(","));
        return sendSubscribeMessage(channels);
    }

    public Mono<Void> unsubscribeToChannels(String channels) {
        return Mono.empty();
    }

    private Mono<Void> processTickerMessage(List<PolygonMessage> messages) {
        messagingTemplate.convertAndSend("/topic/ticker-price", messages);
        return Mono.empty();
    }

    private Mono<Void> processStatusMessage(List<PolygonMessage> messages) {
        StatusMessage statusMessage = (StatusMessage) messages.get(0);
        switch (statusMessage.getStatus()) {
            case "connected":
                return handleConnectedStatus();
            case "auth_success":
                return handleAuthSuccessStatus();
            case "success":
                return handleSubSuccessStatus();
            default:
                return handleUnknownStatus(statusMessage);
        }
    }

    private Mono<Void> handleConnectedStatus() {
        return currentSession.send(Mono.just(currentSession.textMessage(authMessage())));
    }

    private Mono<Void> handleAuthSuccessStatus() {
        log.debug("Authenticated with Polygon WebSocket API");
        return sendSubscribeMessage("AM.LPL,AM.MSFT");
    }

    private Mono<Void> sendSubscribeMessage(String channels) {
        return currentSession.send(Mono.just(currentSession.textMessage(subscribeMessage(channels))));
    }

    private Mono<Void> handleSubSuccessStatus() {
        log.debug("Subscription successful with Polygon WebSocket API");
        return Mono.empty();
    }

    private Mono<Void> handleUnknownStatus(StatusMessage statusMessage) {
        log.error("Unexpected status received from Polygon WebSocket: {}", statusMessage.getStatus());
        return Mono.error(new RuntimeException("Unexpected status: " + statusMessage.getStatus()));
    }

    private List<PolygonMessage> mapToPolygonMessages(String messages) {
        try {
            return objectMapper.readValue(messages, new TypeReference<List<PolygonMessage>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing WebSocket message", e);
        }
    }

    private String authMessage() {
        ActionMessage actionMessage = ActionMessage.builder()
                .action("auth")
                .params(polygonSecret)
                .build();
        try {
            return objectMapper.writeValueAsString(actionMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error creating auth message", e);
        }
    }

    private String subscribeMessage(String params) {
        ActionMessage actionMessage = ActionMessage.builder()
                .action("subscribe")
                .params(params)
                .build();
        try {
            return objectMapper.writeValueAsString(actionMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error creating subscribe message", e);
        }
    }
}
