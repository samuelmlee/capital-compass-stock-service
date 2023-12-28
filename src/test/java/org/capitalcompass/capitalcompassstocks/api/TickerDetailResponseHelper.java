package org.capitalcompass.capitalcompassstocks.api;

public class TickerDetailResponseHelper {
    public static TickerDetailResponse createTickerDetailResponse(TickerDetailResult result) {
        TickerDetailResponse response = new TickerDetailResponse();
        response.setResults(result);
        return response;
    }
}
