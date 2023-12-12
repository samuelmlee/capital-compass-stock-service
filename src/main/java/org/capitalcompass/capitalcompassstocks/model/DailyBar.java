package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DailyBar {

    @JsonProperty("closePrice")
    public Integer getClosePrice() {
        return closePrice;
    }

    @JsonProperty("c")
    public void setClosePrice(Integer closePrice) {
        this.closePrice = closePrice;
    }

    private Integer closePrice;

    @JsonProperty("openPrice")
    public Integer getOpenPrice() {
        return openPrice;
    }

    @JsonProperty("o")
    public void setOpenPrice(Integer openPrice) {
        this.openPrice = openPrice;
    }

    private Integer openPrice;

    @JsonProperty("highestPrice")
    public Integer getHighestPrice() {
        return highestPrice;
    }

    @JsonProperty("h")
    public void setHighestPrice(Integer highestPrice) {
        this.highestPrice = highestPrice;
    }

    private Integer highestPrice;

    @JsonProperty("lowestPrice")
    public Integer getLowestPrice() {
        return lowestPrice;
    }

    @JsonProperty("l")
    public void setLowestPrice(Integer lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    private Integer lowestPrice;

    @JsonProperty("tradingVolume")
    public Integer getTradingVolume() {
        return tradingVolume;
    }

    @JsonProperty("v")
    public void setTradingVolume(Integer tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    private Integer tradingVolume;


    @JsonProperty("volumeWeightedPrice")
    public Integer getVolumeWeightedPrice() {
        return volumeWeightedPrice;
    }

    @JsonProperty("vw")
    public void setVolumeWeightedPrice(Integer volumeWeightedPrice) {
        this.volumeWeightedPrice = volumeWeightedPrice;
    }

    private Integer volumeWeightedPrice;
}
