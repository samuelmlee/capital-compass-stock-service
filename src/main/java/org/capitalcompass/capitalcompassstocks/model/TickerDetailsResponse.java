package org.capitalcompass.capitalcompassstocks.model;

import lombok.Data;


@Data
public class TickerDetailsResponse {
    private TickerDetailsResult results;
    private String status;
}
