package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBar {

    private Integer closePrice;
    private Integer openPrice;
    private Integer highestPrice;
    private Integer lowestPrice;
    private Long tradingVolume;
    private Long volumeWeightedPrice;

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
    public Long getTradingVolume() {
        return tradingVolume;
    }

    @JsonProperty("v")
    public void setTradingVolume(Long tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    @JsonProperty("volumeWeightedPrice")
    public Long getVolumeWeightedPrice() {
        return volumeWeightedPrice;
    }

    @JsonProperty("vw")
    public void setVolumeWeightedPrice(Long volumeWeightedPrice) {
        this.volumeWeightedPrice = volumeWeightedPrice;
    }
}
