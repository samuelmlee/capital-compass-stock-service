package org.capitalcompass.stockservice.messaging;

import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.TickerMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
@Log4j2
public class TickerMessageBroker {

    private final Sinks.Many<TickerMessage> sink;
    private final Flux<TickerMessage> flux;

    public TickerMessageBroker() {
        sink = Sinks.many().multicast().directBestEffort();
        flux = sink.asFlux().publish().autoConnect();
    }

    public Mono<Void> publish(TickerMessage message) {
        try {
            Sinks.EmitResult result = sink.tryEmitNext(message);
            if (!result.isSuccess()) {
                log.error("Unexpected emit result: {} for message : {}", result, message);
            }
        } catch (RuntimeException e) {
            log.error("Unchecked Exception thrown by tryEmitNext for message : {}", message);
        }
        return Mono.empty();
    }

    public Flux<TickerMessage> subscribe() {
        return flux;
    }
}
