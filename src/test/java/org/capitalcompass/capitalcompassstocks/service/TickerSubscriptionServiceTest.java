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

import java.util.Collections;
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

        StepVerifier.create(tickerSubscriptionService.updateSubscriptionsPerClient(messageDTO))
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

        StepVerifier.create(tickerSubscriptionService.updateSubscriptionsPerClient(messageDTO))
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

        Mono<Void> result = tickerSubscriptionService.updateSubscriptionsPerClient(messageDTO)
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

        Mono<Void> result = tickerSubscriptionService.updateSubscriptionsPerClient(messageDTO).then(
                tickerSubscriptionService.removeClientSubscriptions("user1"));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof PolygonWebSocketStateException)
                .verify();

        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(symbols, "subscribe");
        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(symbols, "unsubscribe");
    }

    @Test
    public void updateClientSubscriptionsNewDtoOK() {
        Set<String> initialSymbols = Set.of("AAPL");
        Set<String> newSymbols = Set.of("GOOGL", "MSFT");
        TickerSubscriptionMessageDTO initialDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(initialSymbols)
                .build();
        TickerSubscriptionMessageDTO newDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(newSymbols)
                .build();

        when(webSocketSessionManager.sendSubscriptionMessage(anySet(), anyString())).thenReturn(Mono.empty());

        Mono<Void> initialUpdate = tickerSubscriptionService.updateSubscriptionsPerClient(initialDTO);
        Mono<Void> newUpdate = tickerSubscriptionService.updateSubscriptionsPerClient(newDTO);

        StepVerifier.create(initialUpdate.then(newUpdate))
                .verifyComplete();

        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(initialSymbols, "subscribe");
        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(newSymbols, "subscribe");
        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(initialSymbols, "unsubscribe");
    }

    @Test
    public void updateClientSubscriptionsDecrementOK() {
        Set<String> symbols = Set.of("MSFT");

        TickerSubscriptionMessageDTO firstMessageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        TickerSubscriptionMessageDTO secondMessageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(Collections.emptySet())
                .build();

        when(webSocketSessionManager.sendSubscriptionMessage(anySet(), anyString())).thenReturn(Mono.empty());

        Mono<Void> result = tickerSubscriptionService.updateSubscriptionsPerClient(firstMessageDTO).then(
                tickerSubscriptionService.updateSubscriptionsPerClient(secondMessageDTO));

        StepVerifier.create(result)
                .verifyComplete();

        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(Set.of("MSFT"), "subscribe");
        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(Set.of("MSFT"), "unsubscribe");
    }


    @Test
    public void updateClientSubscriptionsSameDtoOK() {
        Set<String> symbols = Set.of("AAPL");
        TickerSubscriptionMessageDTO messageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        when(webSocketSessionManager.sendSubscriptionMessage(anySet(), anyString())).thenReturn(Mono.empty());

        Mono<Void> result = tickerSubscriptionService.updateSubscriptionsPerClient(messageDTO).then(
                tickerSubscriptionService.updateSubscriptionsPerClient(messageDTO));

        StepVerifier.create(result)
                .verifyComplete();

        verify(webSocketSessionManager, times(1)).sendSubscriptionMessage(anySet(), eq("subscribe"));
        verify(webSocketSessionManager, never()).sendSubscriptionMessage(anySet(), eq("unsubscribe"));
    }


}
