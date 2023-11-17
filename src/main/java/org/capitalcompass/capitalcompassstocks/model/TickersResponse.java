package org.capitalcompass.capitalcompassstocks.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class TickersResponse {
    public ArrayList<TickersResult> results;
    @JsonProperty("request_id")
    public String requestId;
    public int count;
    @JsonProperty("next_url")
    public String nextUrl;
}
