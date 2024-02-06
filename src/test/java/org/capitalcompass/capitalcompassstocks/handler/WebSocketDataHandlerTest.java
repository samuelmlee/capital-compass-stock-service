package org.capitalcompass.capitalcompassstocks.handler;

import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.handler.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebSocketDataHandlerTest {

    @Mock
    private MessageParser messageParser;

    @Mock
    private StatusMessageHandler statusMessageHandler;

    @Mock
    private TickerMessageHandler tickerMessageHandler;

    @Mock
    private DefaultMessageHandler defaultMessageHandler;

    @Mock
    private WebSocketSessionManager webSocketSessionManager;

    @InjectMocks
    private WebSocketDataHandler webSocketDataHandler;

    @Test
    public void handleMessageStatusOK() {
        List<PolygonMessage> mockStatusMessages = List.of(new StatusMessage("status", "connected"));

        when(messageParser.parse(anyString())).thenReturn(mockStatusMessages);
        when(statusMessageHandler.handleMessages(anyList())).thenReturn(Mono.empty());

        Mono<Void> result = webSocketDataHandler.handle(session);

        StepVerifier.create(result).verifyComplete();
        verify(webSocketSessionManager).setWebSocketSession(session);
        verify(statusMessageHandler).handleMessages(mockStatusMessages);

    }


}
