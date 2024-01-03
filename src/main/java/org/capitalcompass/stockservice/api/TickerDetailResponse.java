package org.capitalcompass.stockservice.api;

import lombok.Data;


@Data
public class TickerDetailResponse {
    // results instance variable from ticker details API
    private TickerDetailResult results;
    private String status;
}
