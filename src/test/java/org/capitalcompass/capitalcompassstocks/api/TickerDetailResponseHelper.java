package org.capitalcompass.capitalcompassstocks.api;

import org.capitalcompass.stockservice.api.TickerDetailResponse;
import org.capitalcompass.stockservice.api.TickerDetailResult;

public class TickerDetailResponseHelper {
    public static TickerDetailResponse createTickerDetailResponse(TickerDetailResult result) {
        TickerDetailResponse response = new TickerDetailResponse();
        response.setResults(result);
        return response;
    }
}
