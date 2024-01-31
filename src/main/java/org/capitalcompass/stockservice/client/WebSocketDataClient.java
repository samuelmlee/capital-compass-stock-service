package org.capitalcompass.stockservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.ActionMessage;
import org.capitalcompass.stockservice.api.EventMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class WebSocketDataClient {

    private final WebSocketClient webSocketClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${polygon.websocket.url}")
    private String webSocketUrl;
    @Value("${polygon.api.key}")
    private String polygonSecret;

    public Mono<Void> connectAndSubscribe() {
        return webSocketClient.execute(URI.create(webSocketUrl), this::authenticateAndSubscribe);
    }

    public Mono<Void> subscribeToChannels(WebSocketSession session, String channels) {
        return session.send(Mono.just(session.textMessage(subscribeMessage(channels))));
    }

    private Mono<Void> authenticateAndSubscribe(WebSocketSession session) {
        return session.send(Mono.just(session.textMessage(authMessage())))
                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).log())
                .flatMap(message -> {
                    if (isAuthenticated(message)) {
                        log.info("Authenticated with Poylgon WebSocket API");
                        return subscribeToChannels(session, "AM.LPL,AM.MSFT");
                    } else {
                        return Flux.empty();
                    }
                }).then();
    }

    private boolean isAuthenticated(String messages) {
        try {
            List<EventMessage> eventMessages = objectMapper.readValue(messages, new TypeReference<>() {
            });
            return Objects.equals(eventMessages.get(0).getStatus(), "auth_success");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }
}