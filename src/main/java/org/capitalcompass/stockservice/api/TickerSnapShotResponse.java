package org.capitalcompass.stockservice.api;

import lombok.Data;

@Data
public class TickerSnapShotResponse {

    private TickerSnapshot ticker;
    private String status;
}
