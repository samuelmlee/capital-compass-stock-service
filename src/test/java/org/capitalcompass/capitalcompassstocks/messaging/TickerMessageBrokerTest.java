package org.capitalcompass.capitalcompassstocks.messaging;

import org.capitalcompass.stockservice.api.TickerMessage;
import org.capitalcompass.stockservice.messaging.TickerMessageBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TickerMessageBrokerTest {

    private TickerMessageBroker tickerMessageBroker;
    private Sinks.Many<TickerMessage> mockSink;

    private Flux<TickerMessage> mockFlux;

    @BeforeEach
    public void setUp() {
        tickerMessageBroker = new TickerMessageBroker();
        mockSink = mock(Sinks.Many.class);
        ReflectionTestUtils.setField(tickerMessageBroker, "sink", mockSink);

    }

    @Test
    public void publishResultOK() {
        TickerMessage message = TickerMessage.builder().event("AM").symbol("GOOGL").build();
        when(mockSink.tryEmitNext(message)).thenReturn(Sinks.EmitResult.OK);

        Mono<Void> result = tickerMessageBroker.publish(message);

        StepVerifier.create(result)
                .verifyComplete();

        verify(mockSink, times(1)).tryEmitNext(message);
    }

    @Test
    public void publishResultError() {
        TickerMessage message = TickerMessage.builder().event("AM").symbol("GOOGL").build();
        when(mockSink.tryEmitNext(message)).thenReturn(Sinks.EmitResult.FAIL_TERMINATED);

        Mono<Void> result = tickerMessageBroker.publish(message);

        StepVerifier.create(result)
                .verifyComplete();

        verify(mockSink, times(1)).tryEmitNext(message);
    }

    @Test
    public void publishExceptionError() {
        TickerMessage message = TickerMessage.builder().event("AM").symbol("GOOGL").build();
        when(mockSink.tryEmitNext(message)).thenThrow(new IllegalStateException("Cannot accept messages"));

        Mono<Void> result = tickerMessageBroker.publish(message);

        StepVerifier.create(result)
                .verifyComplete();

        verify(mockSink, times(1)).tryEmitNext(message);
    }
}
