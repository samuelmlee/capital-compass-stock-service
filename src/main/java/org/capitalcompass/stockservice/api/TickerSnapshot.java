package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TickerSnapshot {

    private Long updated;
    private String symbol;
    private DailyBar day;
    private DailyBar prevDay;

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("ticker")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
