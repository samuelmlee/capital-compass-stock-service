package org.capitalcompass.capitalcompassstocks.handler;

import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.api.TickerMessage;
import org.capitalcompass.stockservice.handler.TickerMessageHandler;
import org.capitalcompass.stockservice.messaging.TickerMessageBroker;
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
public class TickerMessageHandlerTest {

    @Mock
    private TickerMessageBroker tickerMessageBroker;

    @InjectMocks
    private TickerMessageHandler tickerMessageHandler;

    @Test
    public void handleMessagesTickerMessagesOK() {
        TickerMessage tickerMessage1 = TickerMessage.builder()
                .event("AM")
                .symbol("AAPL")
                .build();

        TickerMessage tickerMessage2 = TickerMessage.builder()
                .event("AM")
                .symbol("MSFT")
                .build();

        List<PolygonMessage> tickerMessages = List.of(tickerMessage1, tickerMessage2);

        Mono<Void> result = tickerMessageHandler.handleMessages(tickerMessages);

        when(tickerMessageBroker.publish(any(TickerMessage.class))).thenReturn(Mono.empty());

        StepVerifier.create(result).verifyComplete();
        verify(tickerMessageBroker, times(1)).publish(tickerMessage1);
        verify(tickerMessageBroker, times(1)).publish(tickerMessage2);
    }

    @Test
    public void handleMessagesStatusMessagesOK() {
        StatusMessage statusMessage = StatusMessage.builder()
                .event("status")
                .status("connected")
                .build();

        List<PolygonMessage> statusMessages = List.of(statusMessage);

        Mono<Void> result = tickerMessageHandler.handleMessages(statusMessages);

        StepVerifier.create(result).verifyComplete();
        verifyNoInteractions(tickerMessageBroker);
    }


}
