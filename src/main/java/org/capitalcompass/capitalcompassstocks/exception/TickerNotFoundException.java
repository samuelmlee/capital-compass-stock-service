package org.capitalcompass.capitalcompassstocks.exception;

public class TickerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5140217115319895386L;

    public TickerNotFoundException(String s) {
        super(s);
    }
}
