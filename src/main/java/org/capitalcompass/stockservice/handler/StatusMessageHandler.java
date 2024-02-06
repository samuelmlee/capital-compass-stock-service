package org.capitalcompass.stockservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.exception.PolygonMessageUnknownException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class StatusMessageHandler implements PolygonMessageHandler {

    private final WebSocketSessionManager webSocketSessionManager;

    @Override
    public Mono<Void> handleMessages(List<PolygonMessage> messages) {
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
        log.debug("Connected with Polygon WebSocket API");
        return webSocketSessionManager.sendAuthMessage();
    }

    private Mono<Void> handleAuthSuccessStatus() {
        log.debug("Authenticated with Polygon WebSocket API");
        return webSocketSessionManager.sendSubscribeMessage(Set.of("LPL", "MSFT"));
    }

    private Mono<Void> handleSubSuccessStatus() {
        log.debug("Subscription successful with Polygon WebSocket API");
        return Mono.empty();
    }

    private Mono<Void> handleUnknownStatus(StatusMessage statusMessage) {
        log.error("Unexpected status received from Polygon WebSocket: {}", statusMessage.getStatus());
        return Mono.error(new PolygonMessageUnknownException("Unexpected status in message: " + statusMessage));
    }
}
