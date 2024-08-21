package org.capitalcompass.stockservice.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.stockservice.api.DailyBar;

@Data
@Builder
public class TickerSnapshotDTO {
    private Long updated;
    private String symbol;
    private String name;
    private DailyBar day;
    private DailyBar prevDay;
}
