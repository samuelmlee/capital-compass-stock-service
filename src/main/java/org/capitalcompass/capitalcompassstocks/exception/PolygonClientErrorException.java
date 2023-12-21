package org.capitalcompass.capitalcompassstocks.exception;

public class PolygonClientErrorException extends RuntimeException {
    private static final long serialVersionUID = 2130938226342875439L;

    public PolygonClientErrorException(String message) {
        super(message);
    }
}
