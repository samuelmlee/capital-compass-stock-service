package org.capitalcompass.capitalcompassstocks.api;

import org.capitalcompass.stockservice.api.TickerResult;
import org.capitalcompass.stockservice.api.TickersResponse;

import java.util.ArrayList;
import java.util.List;

public class TickersResponseHelper {
    public static TickersResponse createTickersResponse(List<TickerResult> results, Integer count, String nextUrl) {
        TickersResponse response = new TickersResponse();
        response.setResults(new ArrayList<>(results));
        response.setCount(count);
        response.setNextUrl(nextUrl);
        return response;
    }
}
