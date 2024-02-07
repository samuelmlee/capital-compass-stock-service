package org.capitalcompass.capitalcompassstocks.handler;

import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.handler.StatusMessageHandler;
import org.capitalcompass.stockservice.handler.WebSocketSessionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusMessageHandlerTest {

    @Mock
    private WebSocketSessionManager webSocketSessionManager;

    @InjectMocks
    private StatusMessageHandler statusMessageHandler;

    @Test
    public void handleMessagesStatusOK() {
        StatusMessage message = new StatusMessage("status", "connected", "connected to Polygon");

        when(webSocketSessionManager.sendAuthMessage()).thenReturn(Mono.empty());

        Mono<Void> result = statusMessageHandler.handleMessages(List.of(message));

        StepVerifier.create(result).verifyComplete();
        verify(webSocketSessionManager, times(1)).sendAuthMessage();
    }
}
