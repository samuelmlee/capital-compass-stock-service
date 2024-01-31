package org.capitalcompass.stockservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Log4j2
public class WebSocketDataClient {

    private final WebSocketClient webSocketClient;
    private final WebSocketDataHandler webSocketDataHandler;
    @Value("${polygon.websocket.url}")
    private String webSocketUrl;

    public Mono<Void> connectAndAuthenticate() {
        return webSocketClient.execute(URI.create(webSocketUrl),
                webSocketDataHandler
        );
    }
}