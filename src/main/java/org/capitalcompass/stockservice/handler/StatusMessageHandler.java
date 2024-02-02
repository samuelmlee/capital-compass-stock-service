package org.capitalcompass.stockservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.api.StatusMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class StatusMessageHandler implements PolygonMessageHandler {

    private final ControlMessageSender controlMessageSender;

    @Override
    public Mono<Void> handleMessages(List<PolygonMessage> messages, WebSocketSession webSocketSession) {
        StatusMessage statusMessage = (StatusMessage) messages.get(0);
        switch (statusMessage.getStatus()) {
            case "connected":
                return handleConnectedStatus(webSocketSession);
            case "auth_success":
                return handleAuthSuccessStatus(webSocketSession);
            case "success":
                return handleSubSuccessStatus();
            default:
                return handleUnknownStatus(statusMessage);
        }
    }

    private Mono<Void> handleConnectedStatus(WebSocketSession webSocketSession) {
        log.debug("Connected with Polygon WebSocket API");
        return controlMessageSender.sendAuthMessage(webSocketSession);
    }

    private Mono<Void> handleAuthSuccessStatus(WebSocketSession webSocketSession) {
        log.debug("Authenticated with Polygon WebSocket API");
        return controlMessageSender.sendSubscribeMessage(webSocketSession, "AM.LPL,AM.MSFT");
    }

    private Mono<Void> handleSubSuccessStatus() {
        log.debug("Subscription successful with Polygon WebSocket API");
        return Mono.empty();
    }

    private Mono<Void> handleUnknownStatus(StatusMessage statusMessage) {
        log.error("Unexpected status received from Polygon WebSocket: {}", statusMessage.getStatus());
        return Mono.error(new RuntimeException("Unexpected status: " + statusMessage.getStatus()));
    }
}
