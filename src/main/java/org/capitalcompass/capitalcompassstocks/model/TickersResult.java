package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TickersResult {
    public String ticker;
    public String name;
    public String market;
    @JsonProperty("currency_name")
    public String currencyName;
    @JsonProperty("primary_exchange")
    private String primaryExchange;
}
