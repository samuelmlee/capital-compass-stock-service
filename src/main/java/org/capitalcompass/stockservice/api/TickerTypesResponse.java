package org.capitalcompass.stockservice.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerTypesResponse {
    private List<TickerTypeResult> results;
    private int count;
}
