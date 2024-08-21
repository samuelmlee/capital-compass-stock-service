package org.capitalcompass.stockservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PolygonMessageJsonWritingException extends RuntimeException {
    public PolygonMessageJsonWritingException(String s, JsonProcessingException e) {
        super(s, e);
    }
}
