package org.capitalcompass.stockservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.capitalcompass.stockservice.api.ActionMessage;
import org.capitalcompass.stockservice.exception.PolygonMessageJsonWritingException;
import org.capitalcompass.stockservice.exception.PolygonWebSocketStateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WebSocketSessionManager {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${polygon.api.key}")
    private String polygonSecret;
    
    @Value("${ticker.frequency}")
    private String tickerFrequency;

    @Setter
    private WebSocketSession webSocketSession;

    public Mono<Void> sendAuthMessage() {
        if (webSocketSession != null && webSocketSession.isOpen()) {
            return webSocketSession.send(Mono.just(webSocketSession.textMessage(buildAuthMessage())));
        }
        return Mono.error(new PolygonWebSocketStateException("WebSocket session is not open or available."));
    }

    public synchronized Mono<Void> sendSubscriptionMessage(Set<String> channels, String action) {
        if (webSocketSession != null && webSocketSession.isOpen()) {
            return webSocketSession.send(Mono.just(webSocketSession.textMessage(buildSubscriptionMessage(channels, action))));
        }
        return Mono.error(new PolygonWebSocketStateException("WebSocket session is not open or available."));
    }

    private String buildAuthMessage() {
        ActionMessage actionMessage = ActionMessage.builder()
                .action("auth")
                .params(polygonSecret)
                .build();
        try {
            return objectMapper.writeValueAsString(actionMessage);
        } catch (JsonProcessingException e) {
            throw new PolygonMessageJsonWritingException("Error creating auth message", e);
        }
    }

    private String buildSubscriptionMessage(Set<String> channels, String action) {
        String channelsString = channels.stream().map(symbol -> tickerFrequency + "." + symbol).collect(Collectors.joining(","));
        ActionMessage actionMessage = ActionMessage.builder()
                .action(action)
                .params(channelsString)
                .build();
        try {
            return objectMapper.writeValueAsString(actionMessage);
        } catch (JsonProcessingException e) {
            throw new PolygonMessageJsonWritingException("Error creating subscribe message", e);
        }
    }
}
