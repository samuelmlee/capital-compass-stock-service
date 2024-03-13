package org.capitalcompass.stockservice.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CustomReactiveErrorHandling {

    @ExceptionHandler(PolygonClientErrorException.class)
    public ResponseEntity<String> handlePolygonClientErrorException(PolygonClientErrorException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(TickerNotFoundException.class)
    public ResponseEntity<String> handleTickerNotFoundException(TickerNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(TickerDetailRepositoryException.class)
    public ResponseEntity<String> handleTickerDetailRepositoryException(TickerDetailRepositoryException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(PolygonWebSocketStateException.class)
    public ResponseEntity<String> handlePolygonWebSocketStateException(PolygonWebSocketStateException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(TickerMarketDataRepositoryException.class)
    public ResponseEntity<String> handleTickerMarketDataRepositoryException(TickerMarketDataRepositoryException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
