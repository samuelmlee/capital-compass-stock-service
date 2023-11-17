package org.capitalcompass.capitalcompassstocks.exception;

public class PolygonServerErrorException extends RuntimeException {
    public PolygonServerErrorException(String message) {
        super((message));
    }
}
