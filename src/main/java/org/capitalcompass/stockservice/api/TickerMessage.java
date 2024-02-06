package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
public class TickerMessage extends PolygonMessage {

    private String symbol;

    private Integer accumulatedVolume;

    private Integer volumeWeightedPrice;

    private Integer closingTickPrice;

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("sym")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("accumulatedVolume")
    public Integer getAccumulatedVolume() {
        return accumulatedVolume;
    }

    @JsonProperty("av")
    public void setAccumulatedVolume(Integer accumulatedVolume) {
        this.accumulatedVolume = accumulatedVolume;
    }

    @JsonProperty("closingTickPrice")
    public Integer getClosingTickPrice() {
        return closingTickPrice;
    }

    @JsonProperty("c")
    public void setClosingTickPrice(Integer closingTickPrice) {
        this.closingTickPrice = closingTickPrice;
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
