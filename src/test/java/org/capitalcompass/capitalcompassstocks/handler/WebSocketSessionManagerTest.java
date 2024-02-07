package org.capitalcompass.capitalcompassstocks.handler;

import org.capitalcompass.stockservice.exception.PolygonWebSocketStateException;
import org.capitalcompass.stockservice.handler.WebSocketSessionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebSocketSessionManagerTest {
    @Mock
    private WebSocketSession mockWebSocketSession;

    @InjectMocks
    private WebSocketSessionManager webSocketSessionManager;

    @Test
    public void sendAuthMessageOK() {
        String authMessage = "{\"action\":\"auth\",\"params\":null}";

        when(mockWebSocketSession.isOpen()).thenReturn(true);
        when(mockWebSocketSession.send(any(Mono.class))).thenReturn(Mono.empty());
        when(mockWebSocketSession.textMessage(authMessage)).thenReturn(mock(WebSocketMessage.class));

        StepVerifier.create(webSocketSessionManager.sendAuthMessage())
                .verifyComplete();

        verify(mockWebSocketSession, times(1)).send(any());
    }

    @Test
    public void sendAuthMessageError() {
        when(mockWebSocketSession.isOpen()).thenReturn(false);

        StepVerifier.create(webSocketSessionManager.sendAuthMessage())
                .expectError(PolygonWebSocketStateException.class)
                .verify();
    }

    @Test
    void sendSubscribeMessageOK() {

        when(mockWebSocketSession.isOpen()).thenReturn(true);
        when(mockWebSocketSession.send(any(Mono.class))).thenReturn(Mono.empty());
        when(mockWebSocketSession.textMessage(anyString())).thenReturn(mock(WebSocketMessage.class));

        StepVerifier.create(webSocketSessionManager.sendSubscribeMessage(Set.of("AAPL", "MSFT")))
                .verifyComplete();

        verify(mockWebSocketSession, times(1)).send(any());
    }

    @Test
    void sendSubscribeMessageFailure() {
        when(mockWebSocketSession.isOpen()).thenReturn(false);

        StepVerifier.create(webSocketSessionManager.sendSubscribeMessage(Set.of("AAPL", "MSFT")))
                .expectError(PolygonWebSocketStateException.class)
                .verify();
    }

}
