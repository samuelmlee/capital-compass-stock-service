package org.capitalcompass.capitalcompassstocks.model;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class TickerSnapshot {

    private Integer updated;
    private DailyBar day;
    private DailyBar prevDay;
}
