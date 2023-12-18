package org.capitalcompass.capitalcompassstocks.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TickerDetailResult {

    private String symbol;
    private String name;
    private String market;
    private String primaryExchange;
    private String currencyName;
    private String type;
    private String description;
    private Long marketCap;
    private String homePageUrl;
    private Integer totalEmployees;
    private String listDate;
    private Long shareClassSharesOutstanding;
    private Long weightedSharesOutstanding;

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("ticker")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty(value = "primaryExchange")
    public String getPrimaryExchange() {
        return primaryExchange;
    }

    @JsonProperty(value = "primary_exchange")
    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }

    @JsonProperty(value = "currencyName")
    public String getCurrencyName() {
        return currencyName;
    }

    @JsonProperty(value = "currency_name")
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @JsonProperty(value = "marketCap")
    public Long getMarketCap() {
        return marketCap;
    }

    @JsonProperty(value = "market_cap")
    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    @JsonProperty(value = "homepageUrl")
    public String getHomePageUrl() {
        return homePageUrl;
    }

    @JsonProperty(value = "homepage_url")
    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    @JsonProperty(value = "totalEmployees")
    public Integer getTotalEmployees() {
        return totalEmployees;
    }

    @JsonProperty(value = "total_employees")
    public void setTotalEmployees(Integer totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    @JsonProperty(value = "listDate")
    public String getListDate() {
        return listDate;
    }

    @JsonProperty(value = "list_date")
    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    @JsonProperty(value = "shareClassSharesOutstanding")
    public Long getShareClassSharesOutstanding() {
        return shareClassSharesOutstanding;
    }

    @JsonProperty(value = "share_class_shares_outstanding")
    public void setShareClassSharesOutstanding(Long shareClassSharesOutstanding) {
        this.shareClassSharesOutstanding = shareClassSharesOutstanding;
    }

    @JsonProperty(value = "weightedSharesOutstanding")
    public Long getWeightedSharesOutstanding() {
        return weightedSharesOutstanding;
    }

    @JsonProperty(value = "weighted_shares_outstanding")
    public void setWeightedSharesOutstanding(Long weightedSharesOutstanding) {
        this.weightedSharesOutstanding = weightedSharesOutstanding;
    }

}
