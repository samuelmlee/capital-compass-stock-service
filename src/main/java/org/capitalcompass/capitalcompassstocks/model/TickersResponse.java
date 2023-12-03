package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class TickersResponse {
    private ArrayList<TickersResult> results;

    @JsonProperty("request_id")
    private String requestId;

    private Integer count;

    // See https://polygon.io/blog/api-pagination-patterns
    @JsonProperty("next_url")
    private String nextUrl;

}
