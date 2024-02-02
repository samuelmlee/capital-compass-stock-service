package org.capitalcompass.stockservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.ActionMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ControlMessageSender {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${polygon.api.key}")
    private String polygonSecret;

    public Mono<Void> sendAuthMessage(WebSocketSession webSocketSession) {
        return webSocketSession.send(Mono.just(webSocketSession.textMessage(buildAuthMessage())));
    }

    public Mono<Void> sendSubscribeMessage(WebSocketSession webSocketSession, String channels) {
        return webSocketSession.send(Mono.just(webSocketSession.textMessage(buildSubscribeMessage(channels))));
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

    private String buildSubscribeMessage(String params) {
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
