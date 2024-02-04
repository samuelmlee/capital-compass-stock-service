package org.capitalcompass.stockservice.messaging;

import org.capitalcompass.stockservice.api.PolygonMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class TickerMessageBroker {

    private final Sinks.Many<PolygonMessage> sink;
    private final Flux<PolygonMessage> flux;

    public TickerMessageBroker() {
        sink = Sinks.many().multicast().directBestEffort();
        flux = sink.asFlux().publish().autoConnect();
    }

    public Mono<Void> publish(PolygonMessage message) {
        sink.tryEmitNext(message);
        return Mono.empty();
    }

    public Flux<PolygonMessage> subscribe() {
        return flux;
    }
}
