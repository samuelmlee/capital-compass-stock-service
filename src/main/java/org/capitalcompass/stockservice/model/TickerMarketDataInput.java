package org.capitalcompass.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TickerMarketDataInput {
    private String symbol;
    private Long tickerDetailId;

}
