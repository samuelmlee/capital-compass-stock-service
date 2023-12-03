package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TickersResult {
    private String ticker;

    private String name;

    private String market;

    @JsonProperty(value = "currency_name")
    private String currencyName;

    @JsonProperty(value = "primary_exchange")
    private String primaryExchange;
}
