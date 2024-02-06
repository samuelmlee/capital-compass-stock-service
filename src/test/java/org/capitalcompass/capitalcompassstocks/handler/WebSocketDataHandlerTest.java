package org.capitalcompass.capitalcompassstocks.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.exception.PolygonMessageParsingException;
import org.capitalcompass.stockservice.handler.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        WebSocketSession mockSession = mock(WebSocketSession.class);
        WebSocketMessage mockSocketMessage = mock(WebSocketMessage.class);
        List<PolygonMessage> mockStatusMessages = List.of(new StatusMessage("status", "connected", "polygon connection established"));

        when(mockSession.receive()).thenReturn(Flux.just(mockSocketMessage));
        when(mockSocketMessage.getPayloadAsText()).thenReturn("connected");

        when(messageParser.parse(anyString())).thenReturn(mockStatusMessages);
        when(statusMessageHandler.handleMessages(anyList())).thenReturn(Mono.empty());

        Mono<Void> result = webSocketDataHandler.handle(mockSession);

        StepVerifier.create(result).verifyComplete();
        verify(webSocketSessionManager, times(1)).setWebSocketSession(mockSession);
        verify(statusMessageHandler, times(1)).handleMessages(mockStatusMessages);
    }

    @Test
    public void handleMessageTickerOK() {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        WebSocketMessage mockSocketMessage = mock(WebSocketMessage.class);
        List<PolygonMessage> mockStatusMessages = List.of(new StatusMessage("AM", "OK", "Ticker Prices"));

        when(mockSession.receive()).thenReturn(Flux.just(mockSocketMessage));
        when(mockSocketMessage.getPayloadAsText()).thenReturn("Ticker Prices");

        when(messageParser.parse(anyString())).thenReturn(mockStatusMessages);
        when(tickerMessageHandler.handleMessages(anyList())).thenReturn(Mono.empty());

        Mono<Void> result = webSocketDataHandler.handle(mockSession);

        StepVerifier.create(result).verifyComplete();
        verify(webSocketSessionManager, times(1)).setWebSocketSession(mockSession);
        verify(tickerMessageHandler, times(1)).handleMessages(mockStatusMessages);
    }

    @Test
    public void handleMessageUnknownOK() {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        WebSocketMessage mockSocketMessage = mock(WebSocketMessage.class);
        List<PolygonMessage> mockStatusMessages = List.of(new StatusMessage("Error", "Error", "Server Error"));

        when(mockSession.receive()).thenReturn(Flux.just(mockSocketMessage));
        when(mockSocketMessage.getPayloadAsText()).thenReturn("Error");

        when(messageParser.parse(anyString())).thenReturn(mockStatusMessages);
        when(defaultMessageHandler.handleMessages(anyList())).thenReturn(Mono.empty());

        Mono<Void> result = webSocketDataHandler.handle(mockSession);

        StepVerifier.create(result).verifyComplete();
        verify(webSocketSessionManager, times(1)).setWebSocketSession(mockSession);
        verify(defaultMessageHandler, times(1)).handleMessages(mockStatusMessages);
    }

    @Test
    public void handleMessageParserError() {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        WebSocketMessage mockSocketMessage = mock(WebSocketMessage.class);
        JsonProcessingException mockJsonException = mock(JsonProcessingException.class);

        when(mockSession.receive()).thenReturn(Flux.just(mockSocketMessage));
        when(mockSocketMessage.getPayloadAsText()).thenReturn("Unknown format");

        when(messageParser.parse(anyString())).thenThrow(new PolygonMessageParsingException("Parsing failed", mockJsonException));

        Mono<Void> result = webSocketDataHandler.handle(mockSession);

        StepVerifier.create(result).verifyComplete();

        verify(webSocketSessionManager, times(1)).setWebSocketSession(mockSession);
        verifyNoInteractions(statusMessageHandler, tickerMessageHandler, defaultMessageHandler);
    }

    @Test
    public void handleMessageEmptyList() {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        WebSocketMessage mockSocketMessage = mock(WebSocketMessage.class);

        when(mockSession.receive()).thenReturn(Flux.just(mockSocketMessage));
        when(mockSocketMessage.getPayloadAsText()).thenReturn("[]");

        when(messageParser.parse(anyString())).thenReturn(List.of());

        Mono<Void> result = webSocketDataHandler.handle(mockSession);

        StepVerifier.create(result).verifyComplete();
        verify(webSocketSessionManager, times(1)).setWebSocketSession(mockSession);
        verifyNoInteractions(statusMessageHandler, tickerMessageHandler, defaultMessageHandler);
    }

    @Test
    public void handleMessageStatusHandlerError() {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        WebSocketMessage mockSocketMessage = mock(WebSocketMessage.class);
        List<PolygonMessage> mockStatusMessages = List.of(new StatusMessage("status", "Error", "Server Error"));

        when(mockSession.receive()).thenReturn(Flux.just(mockSocketMessage));
        when(mockSocketMessage.getPayloadAsText()).thenReturn("Failure Message");

        when(messageParser.parse(anyString())).thenReturn(mockStatusMessages);
        when(statusMessageHandler.handleMessages(anyList())).thenReturn(Mono.error(new RuntimeException("Handler failure")));

        Mono<Void> result = webSocketDataHandler.handle(mockSession);

        StepVerifier.create(result).verifyComplete();

        verify(webSocketSessionManager, times(1)).setWebSocketSession(mockSession);
        verify(statusMessageHandler, times(1)).handleMessages(mockStatusMessages);
        verifyNoInteractions(tickerMessageHandler, defaultMessageHandler);
    }


}
