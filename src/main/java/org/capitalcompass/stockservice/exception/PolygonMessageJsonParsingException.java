package org.capitalcompass.stockservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PolygonMessageJsonParsingException extends RuntimeException {
    public PolygonMessageJsonParsingException(String s, JsonProcessingException e) {
        super(s, e);
    }
}
