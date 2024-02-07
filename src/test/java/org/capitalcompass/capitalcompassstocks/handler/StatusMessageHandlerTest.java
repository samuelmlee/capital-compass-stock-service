package org.capitalcompass.capitalcompassstocks.handler;

import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.exception.PolygonMessageUnknownException;
import org.capitalcompass.stockservice.handler.StatusMessageHandler;
import org.capitalcompass.stockservice.handler.WebSocketSessionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class StatusMessageHandlerTest {

    @Mock
    private WebSocketSessionManager webSocketSessionManager;

    @InjectMocks
    private StatusMessageHandler statusMessageHandler;

    @Test
    public void handleMessagesConnectedOK() {
        StatusMessage message = new StatusMessage("status", "connected", "connected to Polygon");

        when(webSocketSessionManager.sendAuthMessage()).thenReturn(Mono.empty());

        Mono<Void> result = statusMessageHandler.handleMessages(List.of(message));

        StepVerifier.create(result).verifyComplete();
        verify(webSocketSessionManager, times(1)).sendAuthMessage();
    }

    @Test
    public void handleMessagesAuthOK(CapturedOutput output) {
        StatusMessage message = new StatusMessage("status", "auth_success", "authenticated successfully");

        Mono<Void> result = statusMessageHandler.handleMessages(List.of(message));

        StepVerifier.create(result).verifyComplete();

        assertThat(output.getOut()).contains("Authenticated with Polygon WebSocket API");

        verifyNoInteractions(webSocketSessionManager);
    }

    @Test
    public void handleMessagesSubOK(CapturedOutput output) {
        StatusMessage message = new StatusMessage("status", "success", "subscription successful");

        Mono<Void> result = statusMessageHandler.handleMessages(List.of(message));

        StepVerifier.create(result).verifyComplete();

        assertThat(output.getOut()).contains("Subscription successful with Polygon WebSocket API");

        verifyNoInteractions(webSocketSessionManager);
    }

    @Test
    public void handleMessagesUnknownError(CapturedOutput output) {
        StatusMessage message = new StatusMessage("status", "unknown_status", "message");

        Mono<Void> result = statusMessageHandler.handleMessages(List.of(message));

        StepVerifier.create(result)
                .expectError(PolygonMessageUnknownException.class)
                .verify();

        assertThat(output.getOut()).contains("Unexpected status received from Polygon WebSocket: unknown_status");

        verifyNoInteractions(webSocketSessionManager);
    }

    @Test
    public void handleMessagesEmptyOK() {
        Mono<Void> result = statusMessageHandler.handleMessages(Collections.emptyList());

        StepVerifier.create(result).verifyComplete();

        verifyNoInteractions(webSocketSessionManager);
    }


}
