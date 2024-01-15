package org.capitalcompass.stockservice.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerDetailResponse {
    // results instance variable from ticker details API
    private TickerDetailResult results;
    private String status;
}
