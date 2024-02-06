package org.capitalcompass.stockservice.handler;

import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Log4j2
public class DefaultMessageHandler implements PolygonMessageHandler {
    @Override
    public Mono<Void> handleMessages(List<PolygonMessage> messages) {
        log.info("Unknown Polygon message :" + messages.toString());
        return Mono.empty();
    }
}
