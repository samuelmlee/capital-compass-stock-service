package org.capitalcompass.stockservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.handler.WebSocketDataHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.Disposable;

import javax.annotation.PostConstruct;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Log4j2
public class WebSocketDataClient {

    private final WebSocketClient webSocketClient;
    private final WebSocketDataHandler webSocketDataHandler;
    @Value("${polygon.websocket.url}")
    private String webSocketUrl;

    @PostConstruct
    public Disposable connectAndAuthenticate() {
        return webSocketClient.execute(URI.create(webSocketUrl),
                webSocketDataHandler
        ).subscribe();
    }
}