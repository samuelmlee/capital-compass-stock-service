package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TickerDetailsResult {
    private String ticker;

    private String name;

    private String market;

    @JsonProperty(value = "primaryExchange")
    public String getPrimaryExchange() {
        return primaryExchange;
    }

    @JsonProperty(value = "primary_exchange")
    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }

    private String primaryExchange;


    @JsonProperty(value = "currencyName")
    public String getCurrencyName() {
        return currencyName;
    }

    @JsonProperty(value = "currency_name")
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    private String currencyName;

    private String type;

    private String description;

    @JsonProperty(value = "marketCap")
    public Long getMarketCap() {
        return marketCap;
    }

    @JsonProperty(value = "market_cap")
    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    private Long marketCap;

    @JsonProperty(value = "homepageUrl")
    public String getHomePageUrl() {
        return homePageUrl;
    }

    @JsonProperty(value = "homepage_url")
    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    private String homePageUrl;

    @JsonProperty(value = "totalEmployees")
    public Integer getTotalEmployees() {
        return totalEmployees;
    }

    @JsonProperty(value = "total_employees")
    public void setTotalEmployees(Integer totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    private Integer totalEmployees;

    @JsonProperty(value = "listDate")
    public String getListDate() {
        return listDate;
    }

    @JsonProperty(value = "list_date")
    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    private String listDate;

    @JsonProperty(value = "shareClassSharesOutstanding")
    public Long getShareClassSharesOutstanding() {
        return shareClassSharesOutstanding;
    }

    @JsonProperty(value = "share_class_shares_outstanding")
    public void setShareClassSharesOutstanding(Long shareClassSharesOutstanding) {
        this.shareClassSharesOutstanding = shareClassSharesOutstanding;
    }

    private Long shareClassSharesOutstanding;

    @JsonProperty(value = "weightedSharesOutstanding")
    public Long getWeightedSharesOutstanding() {
        return weightedSharesOutstanding;
    }

    @JsonProperty(value = "weighted_shares_outstanding")
    public void setWeightedSharesOutstanding(Long weightedSharesOutstanding) {
        this.weightedSharesOutstanding = weightedSharesOutstanding;
    }

    private Long weightedSharesOutstanding;

}
