package org.capitalcompass.capitalcompassstocks.api;

import lombok.Data;


@Data
public class TickerDetailResponse {
    private TickerDetailResult result;
    private String status;
}
