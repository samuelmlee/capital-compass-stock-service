package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerNewsResponse {

    private ArrayList<TickerNewsResult> results;
    private int count;
    private String nextUrl;


    @JsonProperty("next_url")
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }


}
