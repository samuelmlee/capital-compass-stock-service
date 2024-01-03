package org.capitalcompass.stockservice.exception;

public class TickerNotFoundException extends RuntimeException {

    public TickerNotFoundException(String s) {
        super(s);
    }
}
