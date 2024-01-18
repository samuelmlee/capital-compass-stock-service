package org.capitalcompass.stockservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TickerTypeResult {
    private String code;
    private String description;
}
