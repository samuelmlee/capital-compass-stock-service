package org.capitalcompass.stockservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketDataHandler implements WebSocketHandler {

    private final MessageParser messageParser;

    private final StatusMessageHandler statusMessageHandler;

    private final TickerMessageHandler tickerMessageHandler;

    private final DefaultMessageHandler defaultMessageHandler;


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive().map(WebSocketMessage::getPayloadAsText).log()
                .flatMap(messageString -> {
                    List<PolygonMessage> messages = messageParser.parse(messageString);

                    PolygonMessageHandler handler = resolveMessageHandler(messages.get(0).getEvent());
                    return handler.handleMessages(messages, session);
                }).then();
    }

    private PolygonMessageHandler resolveMessageHandler(String eventType) {
        switch (eventType) {
            case "status":
                return statusMessageHandler;
            case "AM":
                return tickerMessageHandler;
            default:
                return defaultMessageHandler;
        }
    }
}
