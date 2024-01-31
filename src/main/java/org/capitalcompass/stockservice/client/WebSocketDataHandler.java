package org.capitalcompass.stockservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.ActionMessage;
import org.capitalcompass.stockservice.api.EventMessage;
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
                    List<EventMessage> eventMessages = mapToEventMessages(messageString);

                    if (Objects.equals(eventMessages.get(0).getStatus(), "connected")) {
                        return session.send(Mono.just(session.textMessage(authMessage())));
                    }

                    if (Objects.equals(eventMessages.get(0).getStatus(), "auth_success")) {
                        log.info("Authenticated with Polygon WebSocket API");
//                        return subscribeToChannels("AM.LPL,AM.MSFT");
                    } else {
                        log.error("Authentication with Polygon WebSocket failed");
                        return Mono.error(new RuntimeException("Authentication failed"));
                    }
                    return Mono.empty();
                }).then();
    }

    public Mono<Void> subscribeToChannels(String channels) {
        return currentSession.send(Mono.just(currentSession.textMessage(subscribeMessage(channels))));
    }

    private List<EventMessage> mapToEventMessages(String messages) {
        try {
            return objectMapper.readValue(messages, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error creating Event message", e);
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
