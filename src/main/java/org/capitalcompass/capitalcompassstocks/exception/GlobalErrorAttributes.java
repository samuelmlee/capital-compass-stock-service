package org.capitalcompass.capitalcompassstocks.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                  ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        Throwable error = super.getError(request);

        if (error instanceof PolygonClientErrorException) {
            map.put("status", HttpStatus.BAD_REQUEST);
            map.put("customMessage", "The request parameters are invalid : " + error.getMessage());
            return map;
        }
        if (error instanceof PolygonServerErrorException) {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            map.put("customMessage", "Polygon API server error");
            return map;
        }

        return map;
    }

}
