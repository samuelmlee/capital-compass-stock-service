package org.capitalcompass.stockservice.api;

import lombok.Data;

import java.util.List;

@Data
public class AllTickerSnapshotsResponse {
    private List<TickerSnapshot> tickers;
    private String status;
}
