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

        when(webSocketSessionManager.sendSubscribeMessage(symbols)).thenReturn(Mono.empty());

        StepVerifier.create(tickerSubscriptionService.updateClientSubscriptions(messageDTO))
                .verifyComplete();

        verify(webSocketSessionManager, times(1)).sendSubscribeMessage(symbols);
    }

    @Test
    public void updateClientSubscriptionsError() {
        Set<String> symbols = Set.of("AAPL", "MSFT");
        TickerSubscriptionMessageDTO messageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        when(webSocketSessionManager.sendSubscribeMessage(symbols)).thenReturn(Mono.error(
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

        when(webSocketSessionManager.sendSubscribeMessage(anySet())).thenReturn(Mono.empty());

        tickerSubscriptionService.updateClientSubscriptions(messageDTO).subscribe();

        StepVerifier.create(tickerSubscriptionService.removeClientSubscriptions("user1"))
                .verifyComplete();

        verify(webSocketSessionManager, times(1)).sendSubscribeMessage(symbols);
        verify(webSocketSessionManager, times(1)).sendSubscribeMessage(Set.of());
    }

    @Test
    public void removeClientSubscriptionsError() {
        Set<String> symbols = Set.of("AAPL", "MSFT");
        TickerSubscriptionMessageDTO messageDTO = TickerSubscriptionMessageDTO.builder()
                .userId("user1")
                .symbols(symbols)
                .build();

        when(webSocketSessionManager.sendSubscribeMessage(anySet()))
                .thenReturn(Mono.empty())
                .thenReturn(Mono.error(new PolygonWebSocketStateException("WebSocket session is not open or available.")));

        tickerSubscriptionService.updateClientSubscriptions(messageDTO).subscribe();

        StepVerifier.create(tickerSubscriptionService.updateClientSubscriptions(messageDTO))
                .expectErrorMatches(throwable -> throwable instanceof PolygonWebSocketStateException)
                .verify();

        verify(webSocketSessionManager, times(2)).sendSubscribeMessage(anySet());
    }


}
