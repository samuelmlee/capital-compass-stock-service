package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AggregatesResult {
    @JsonProperty("t")
    private Long timestamp;

    @JsonProperty("c")
    private Integer closingPrice;
}
