package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class TickersResponse {
    private ArrayList<TickerResult> results;

    private Integer count;
    // See https://polygon.io/blog/api-pagination-patterns
    private String nextUrl;

    @JsonProperty(value = "next_url")
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

}
