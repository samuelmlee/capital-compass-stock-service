package org.capitalcompass.stockservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.ActionMessage;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.api.StatusMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Log4j2
@Component
public class WebSocketDataHandler implements WebSocketHandler {

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

    private Mono<Void> processTickerMessage(List<PolygonMessage> messages) {
        return Mono.empty();
    }

    private Mono<Void> processStatusMessage(List<PolygonMessage> messages) {
        StatusMessage statusMessage = (StatusMessage) messages.get(0);

        if (Objects.equals(statusMessage.getStatus(), "connected")) {
            return currentSession.send(Mono.just(currentSession.textMessage(authMessage())));
        }
        if (Objects.equals(statusMessage.getStatus(), "auth_success")) {
            log.debug("Authenticated with Polygon WebSocket API");
            return subscribeToChannels("AM.LPL,AM.MSFT");
        }
        if (Objects.equals(statusMessage.getStatus(), "success")) {
            log.debug("Subscription successful with Polygon WebSocket API");
            return Mono.empty();
        }
        log.error("Authentication with Polygon WebSocket failed");
        return Mono.error(new RuntimeException("Authentication failed"));
//        return Mono.empty();
    }


    public Mono<Void> subscribeToChannels(String channels) {
        return currentSession.send(Mono.just(currentSession.textMessage(subscribeMessage(channels))));
    }

    public Mono<Void> unsubscribeToChannels(String channels) {
        return Mono.empty();
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
