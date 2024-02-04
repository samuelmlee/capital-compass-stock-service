package org.capitalcompass.stockservice.messaging;

import org.capitalcompass.stockservice.api.TickerMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class TickerMessageBroker {

    private final Sinks.Many<TickerMessage> sink;
    private final Flux<TickerMessage> flux;

    public TickerMessageBroker() {
        sink = Sinks.many().multicast().directBestEffort();
        flux = sink.asFlux().publish().autoConnect();
    }

    public Mono<Void> publish(TickerMessage message) {
        sink.tryEmitNext(message);
        return Mono.empty();
    }

    public Flux<TickerMessage> subscribe() {
        return flux;
    }
}
