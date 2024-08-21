package org.capitalcompass.stockservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.handler.WebSocketDataHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.Disposable;

import jakarta.annotation.PostConstruct;
import java.net.URI;

/**
 * Client responsible for establishing and maintaining a WebSocket connection
 * with a specified WebSocket URL. This client uses a {@link WebSocketDataHandler}
 * to handle the data received through the WebSocket connection.
 * <p>
 * This client is intended to connect to the Polygon WebSocket API for real-time
 * financial data streaming, but can be adapted for other WebSocket APIs as well.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class WebSocketDataClient {

    /**
     * The {@link WebSocketClient} used to establish the WebSocket connection.
     */
    private final WebSocketClient webSocketClient;

    /**
     * The {@link WebSocketDataHandler} used to handle data received through the
     * WebSocket connection.
     */
    private final WebSocketDataHandler webSocketDataHandler;

    /**
     * The URL of the WebSocket API, injected from AWS.
     */
    @Value("${polygon.websocket.url}")
    private String webSocketUrl;

    /**
     * Initializes the WebSocket connection to the specified URL and starts
     * listening for messages using the provided {@link WebSocketDataHandler}.
     * This method is automatically called after the bean's properties have been
     * set by Spring's dependency injection facilities.
     *
     * @return A {@link Disposable} that represents the active WebSocket session.
     * This can be used to dispose of the connection when it's no longer needed.
     */
    @PostConstruct
    public Disposable connectAndAuthenticate() {
        return webSocketClient.execute(URI.create(webSocketUrl),
                webSocketDataHandler
        ).subscribe();
    }
}