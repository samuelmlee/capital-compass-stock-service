package org.capitalcompass.stockservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.ActionMessage;
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

    private WebSocketSession webSocketSession;

    public synchronized void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public synchronized Mono<Void> sendAuthMessage() {
        if (webSocketSession != null && webSocketSession.isOpen()) {
            return webSocketSession.send(Mono.just(webSocketSession.textMessage(buildAuthMessage())));
        }
        return Mono.error(new IllegalStateException("WebSocket session is not open or available."));
    }

    public synchronized Mono<Void> sendSubscribeMessage(Set<String> channels) {
        if (webSocketSession != null && webSocketSession.isOpen()) {
            return webSocketSession.send(Mono.just(webSocketSession.textMessage(buildSubscribeMessage(channels))));
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
            throw new RuntimeException("Error creating auth message", e);
        }
    }

    private String buildSubscribeMessage(Set<String> channels) {
        String channelsString = channels.stream().map(symbol -> "AM." + symbol).collect(Collectors.joining(","));
        ActionMessage actionMessage = ActionMessage.builder()
                .action("subscribe")
                .params(channelsString)
                .build();
        try {
            return objectMapper.writeValueAsString(actionMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error creating subscribe message", e);
        }
    }
}
