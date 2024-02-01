package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "ev", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TickerMessage.class, name = "T"),
        @JsonSubTypes.Type(value = StatusMessage.class, name = "status")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class PolygonMessage {
    @JsonProperty("ev")
    private String event;
}
