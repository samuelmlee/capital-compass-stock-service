package org.capitalcompass.stockservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.exception.PolygonMessageJsonParsingException;
import org.capitalcompass.stockservice.exception.PolygonMessageUnknownException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketDataHandler implements WebSocketHandler {

    private final MessageParser messageParser;

    private final StatusMessageHandler statusMessageHandler;

    private final TickerMessageHandler tickerMessageHandler;

    private final DefaultMessageHandler defaultMessageHandler;

    private final WebSocketSessionManager webSocketSessionManager;


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        webSocketSessionManager.setWebSocketSession(session);
        return session.receive()
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(5))
                        .doBeforeRetry(retrySignal -> log.info("Attempting to reconnect. Retry attempt: {}", retrySignal.totalRetries()))
                )
                .map(WebSocketMessage::getPayloadAsText).log()
                .flatMap(messageString -> {
                    List<PolygonMessage> messages;
                    try {
                        messages = messageParser.parse(messageString);
                    } catch (PolygonMessageJsonParsingException e) {
                        log.error("Error parsing message: {}. Continuing with stream.", messageString, e);
                        return Mono.empty();
                    }
                    if (messages != null && messages.isEmpty()) {
                        return Mono.empty();
                    }
                    PolygonMessageHandler handler = resolveMessageHandler(messages.get(0).getEvent());
                    return handler.handleMessages(messages);
                })
                .onErrorContinue((throwable, o) -> {
                    if (throwable instanceof PolygonMessageUnknownException) {
                        log.error("Continuing after encountering Polygon unknown message: {}", throwable.getMessage());
                    } else {
                        log.error("Continuing after error: {}", throwable.getMessage());
                    }
                })
                .then();
    }

    private PolygonMessageHandler resolveMessageHandler(String eventType) {
        switch (eventType) {
            case "status":
                return statusMessageHandler;
            case "A":
            case "AM":
                return tickerMessageHandler;
            default:
                return defaultMessageHandler;
        }
    }
}
