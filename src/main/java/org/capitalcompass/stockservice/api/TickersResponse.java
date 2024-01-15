package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickersResponse {
    private List<TickerResult> results;

    private Integer count;
    // See https://polygon.io/blog/api-pagination-patterns
    private String nextUrl;

    @JsonProperty(value = "next_url")
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

}
