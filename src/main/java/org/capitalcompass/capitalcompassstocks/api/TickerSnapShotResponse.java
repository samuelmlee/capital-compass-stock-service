package org.capitalcompass.capitalcompassstocks.api;

import lombok.Data;

@Data
public class TickerSnapShotResponse {

    private TickerSnapshot ticker;
    private String status;
}
