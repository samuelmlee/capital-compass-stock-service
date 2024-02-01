package org.capitalcompass.stockservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.client.WebSocketDataClient;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
@RequiredArgsConstructor
public class WebSocketDataController {

    private final WebSocketDataClient webSocketDataClient;

//    @MessageMapping("/aggregate")
//    public Flux<OutputMessage> message(InputMessage message) {
//        log.debug("Input Message "+message);
//        return OutputMessage
//                .builder()
//                .content(message.getContent())
//                .build();
//    }
}
