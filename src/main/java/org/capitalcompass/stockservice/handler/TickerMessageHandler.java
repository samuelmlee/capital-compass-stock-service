package org.capitalcompass.stockservice.handler;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.messaging.TickerMessageBroker;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TickerMessageHandler implements PolygonMessageHandler {

    private final TickerMessageBroker tickerMessageBroker;

    @Override
    public Mono<Void> handleMessages(List<PolygonMessage> messages) {
        return Flux.fromIterable(messages).flatMap(tickerMessageBroker::publish).then();
    }
}
