package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AggregatesResult {
    @JsonProperty("t")
    public long timestamp;

    @JsonProperty("c")
    public int closingPrice;
}
