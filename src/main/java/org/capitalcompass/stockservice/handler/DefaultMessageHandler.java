package org.capitalcompass.stockservice.handler;

import org.capitalcompass.stockservice.api.PolygonMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DefaultMessageHandler implements PolygonMessageHandler {
    @Override
    public Mono<Void> handleMessages(List<PolygonMessage> messages) {
        return Mono.empty();
    }
}
