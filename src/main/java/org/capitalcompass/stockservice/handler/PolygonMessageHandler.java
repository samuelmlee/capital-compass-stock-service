package org.capitalcompass.stockservice.handler;

import org.capitalcompass.stockservice.api.PolygonMessage;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PolygonMessageHandler {

    Mono<Void> handleMessages(List<PolygonMessage> messages);
}
