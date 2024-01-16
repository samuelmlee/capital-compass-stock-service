package org.capitalcompass.stockservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TickerTypeResult {
    private String code;
    private String description;
}
