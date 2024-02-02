package org.capitalcompass.stockservice.handler;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TickerMessageHandler implements PolygonMessageHandler {

    private final SimpMessagingTemplate messagingTemplate;
    
    @Override
    public Mono<Void> handleMessages(List<PolygonMessage> messages, WebSocketSession webSocketSession) {
        messagingTemplate.convertAndSend("/topic/ticker-price", messages);
        return Mono.empty();
    }
}