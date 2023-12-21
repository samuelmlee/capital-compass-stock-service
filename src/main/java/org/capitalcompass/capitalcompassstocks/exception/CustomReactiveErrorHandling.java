package org.capitalcompass.capitalcompassstocks.exception;

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
}
