package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TickerMessage extends PolygonMessage {

    @JsonProperty("sym")
    private String symbol;

    @JsonProperty("v")
    private Integer volume;

    @JsonProperty("o")
    private Integer openingTickPrice;

    @JsonProperty("a")
    private Integer averagePrice;

    @JsonProperty("c")
    private Integer closingTickPrice;

}
