package org.capitalcompass.capitalcompassstocks.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickerDetailsResponse {
    private TickersResult results;
    private String status;
}
