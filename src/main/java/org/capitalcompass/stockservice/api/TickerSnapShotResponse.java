package org.capitalcompass.stockservice.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerSnapShotResponse {

    private TickerSnapshot ticker;
    private String status;
}
