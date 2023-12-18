package org.capitalcompass.capitalcompassstocks.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DailyBar {

    private Integer closePrice;
    private Integer openPrice;
    private Integer highestPrice;
    private Integer lowestPrice;
    private Integer tradingVolume;
    private Integer volumeWeightedPrice;

    @JsonProperty("closePrice")
    public Integer getClosePrice() {
        return closePrice;
    }

    @JsonProperty("c")
    public void setClosePrice(Integer closePrice) {
        this.closePrice = closePrice;
    }

    @JsonProperty("openPrice")
    public Integer getOpenPrice() {
        return openPrice;
    }

    @JsonProperty("o")
    public void setOpenPrice(Integer openPrice) {
        this.openPrice = openPrice;
    }

    @JsonProperty("highestPrice")
    public Integer getHighestPrice() {
        return highestPrice;
    }

    @JsonProperty("h")
    public void setHighestPrice(Integer highestPrice) {
        this.highestPrice = highestPrice;
    }

    @JsonProperty("lowestPrice")
    public Integer getLowestPrice() {
        return lowestPrice;
    }

    @JsonProperty("l")
    public void setLowestPrice(Integer lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    @JsonProperty("tradingVolume")
    public Integer getTradingVolume() {
        return tradingVolume;
    }

    @JsonProperty("v")
    public void setTradingVolume(Integer tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    @JsonProperty("volumeWeightedPrice")
    public Integer getVolumeWeightedPrice() {
        return volumeWeightedPrice;
    }

    @JsonProperty("vw")
    public void setVolumeWeightedPrice(Integer volumeWeightedPrice) {
        this.volumeWeightedPrice = volumeWeightedPrice;
    }
}
