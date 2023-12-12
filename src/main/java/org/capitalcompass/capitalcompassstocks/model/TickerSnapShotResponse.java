package org.capitalcompass.capitalcompassstocks.model;

import lombok.Data;

@Data
public class TickerSnapShotResponse {

    private TickerSnapshot ticker;
    private String status;
}
