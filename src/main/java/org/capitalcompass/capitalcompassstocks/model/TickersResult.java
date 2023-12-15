package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TickersResult {
    private String symbol;

    private String name;

    private String market;
    private String currencyName;
    private String primaryExchange;

    @JsonProperty("currencyName")
    public String getCurrencyName() {
        return currencyName;
    }

    @JsonProperty("currency_name")
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @JsonProperty("primaryExchange")
    public String getPrimaryExchange() {
        return primaryExchange;
    }

    @JsonProperty("primary_exchange")
    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }
}
