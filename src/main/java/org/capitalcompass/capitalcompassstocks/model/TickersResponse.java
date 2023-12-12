package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class TickersResponse {
    private ArrayList<TickersResult> results;

    private Integer count;

    @JsonProperty(value = "next_url")
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    // See https://polygon.io/blog/api-pagination-patterns
    private String nextUrl;

}
