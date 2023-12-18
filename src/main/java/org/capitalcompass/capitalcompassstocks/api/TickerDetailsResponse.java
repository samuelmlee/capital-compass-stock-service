package org.capitalcompass.capitalcompassstocks.api;

import lombok.Data;


@Data
public class TickerDetailsResponse {
    private TickerDetailsResult results;
    private String status;
}
