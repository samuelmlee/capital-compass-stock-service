package org.capitalcompass.capitalcompassstocks.service;

import org.capitalcompass.stockservice.dto.TickerSubscriptionMessageDTO;
import org.capitalcompass.stockservice.exception.PolygonWebSocketStateException;
import org.capitalcompass.stockservice.handler.WebSocketSessionManager;
import org.capitalcompass.stockservice.service.TickerSubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TickerSubscriptionServiceTest {

    @Mock
    private WebSocketSessionManager webSocketSessionManager;

    @InjectMocks
    private TickerSubscriptionService tickerSubscriptionService;

    @Test
    public void updateClientSubscriptionsOK() {
        Set<String> symbols = Set.of("AAPL", "MSFT");
        TickerSubscriptionMessageDTO messageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        when(webSocketSessionManager.sendSubscriptionMessage(symbols, "subscribe")).thenReturn(Mono.empty());

        StepVerifier.create(tickerSubscriptionService.updateClientSubscriptions(messageDTO))
                .verifyComplete();

        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(symbols, "subscribe");
    }

    @Test
    public void updateClientSubscriptionsError() {
        Set<String> symbols = Set.of("AAPL", "MSFT");
        TickerSubscriptionMessageDTO messageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        when(webSocketSessionManager.sendSubscriptionMessage(symbols, "subscribe")).thenReturn(Mono.error(
                new PolygonWebSocketStateException("WebSocket session is not open or available.")));

        StepVerifier.create(tickerSubscriptionService.updateClientSubscriptions(messageDTO))
                .expectErrorMatches(throwable -> throwable instanceof PolygonWebSocketStateException)
                .verify();
    }

    @Test
    public void removeClientSubscriptionsOK() {
        Set<String> symbols = Set.of("AAPL", "MSFT");
        TickerSubscriptionMessageDTO messageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        when(webSocketSessionManager.sendSubscriptionMessage(anySet(), anyString())).thenReturn(Mono.empty());

        Mono<Void> result = tickerSubscriptionService.updateClientSubscriptions(messageDTO)
                .then(tickerSubscriptionService.removeClientSubscriptions("user1"));

        StepVerifier.create(result)
                .verifyComplete();

        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(symbols, "subscribe");
        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(symbols, "unsubscribe");
    }

    @Test
    public void removeClientSubscriptionsError() {
        Set<String> symbols = Set.of("AAPL", "MSFT");
        TickerSubscriptionMessageDTO messageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        when(webSocketSessionManager.sendSubscriptionMessage(anySet(), anyString()))
                .thenReturn(Mono.empty())
                .thenReturn(Mono.error(new PolygonWebSocketStateException("WebSocket session is not open or available.")));

        Mono<Void> result = tickerSubscriptionService.updateClientSubscriptions(messageDTO).then(
                tickerSubscriptionService.removeClientSubscriptions("user1"));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof PolygonWebSocketStateException)
                .verify();

        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(symbols, "subscribe");
        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(symbols, "unsubscribe");
    }


}
