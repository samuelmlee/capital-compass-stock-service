package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TickerDetailsResult {
    private String ticker;

    private String name;

    private String market;

    @JsonProperty(value = "primary_exchange")
    private String primaryExchange;

    @JsonProperty(value = "currency_name")
    private String currencyName;

    private String type;

    private String description;

    @JsonProperty(value = "market_cap")
    private Long marketCap;

    @JsonProperty(value = "homepage_url")
    private String homePageUrl;

    @JsonProperty(value = "total_employees")
    private Integer totalEmployees;

    @JsonProperty(value = "list_date")
    private String listDate;
    
    @JsonProperty(value = "share_class_shares_outstanding")
    private Long shareClassSharesOutstanding;

    @JsonProperty(value = "weighted_shares_outstanding")
    private Long weightedSharesOutstanding;

}
