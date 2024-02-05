package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerMessage extends PolygonMessage {

    private String symbol;

    private Integer volume;

    private Integer openingTickPrice;

    private Integer averagePrice;

    private Integer closingTickPrice;

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("sym")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("volume")
    public Integer getVolume() {
        return volume;
    }

    @JsonProperty("v")
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @JsonProperty("openingTickPrice")
    public Integer getOpeningTickPrice() {
        return openingTickPrice;
    }

    @JsonProperty("o")
    public void setOpeningTickPrice(Integer openingTickPrice) {
        this.openingTickPrice = openingTickPrice;
    }

    @JsonProperty("averagePrice")
    public Integer getAveragePrice() {
        return averagePrice;
    }

    @JsonProperty("a")
    public void setAveragePrice(Integer averagePrice) {
        this.averagePrice = averagePrice;
    }

    @JsonProperty("closingTickPrice")
    public Integer getClosingTickPrice() {
        return closingTickPrice;
    }

    @JsonProperty("c")
    public void setClosingTickPrice(Integer closingTickPrice) {
        this.closingTickPrice = closingTickPrice;
    }


}
