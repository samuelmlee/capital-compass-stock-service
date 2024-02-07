package org.capitalcompass.stockservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.capitalcompass.stockservice.api.PolygonMessage;
import org.capitalcompass.stockservice.exception.PolygonMessageJsonParsingException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PolygonMessage> parse(String messages) {
        try {
            return objectMapper.readValue(messages, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new PolygonMessageJsonParsingException("Error deserializing Polygon WebSocket message", e);
        }
    }
}
