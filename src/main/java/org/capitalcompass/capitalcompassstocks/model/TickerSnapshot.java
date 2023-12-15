package org.capitalcompass.capitalcompassstocks.model;

import lombok.Data;

@Data
public class TickerSnapshot {

    private Long updated;
    private String symbol;
    private DailyBar day;
    private DailyBar prevDay;
}
