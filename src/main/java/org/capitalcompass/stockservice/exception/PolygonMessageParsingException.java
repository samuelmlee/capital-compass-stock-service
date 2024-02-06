package org.capitalcompass.stockservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PolygonMessageParsingException extends RuntimeException {
    public PolygonMessageParsingException(String s, JsonProcessingException e) {
        super(s, e);
    }
}
