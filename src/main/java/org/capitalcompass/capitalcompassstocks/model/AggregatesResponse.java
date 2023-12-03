package org.capitalcompass.capitalcompassstocks.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AggregatesResponse {
    private String ticker;

    private Integer queryCount;

    private Integer resultsCount;

    private Boolean adjusted;
    private ArrayList<AggregatesResult> results;

    private String status;

    private String request_id;

    private Integer count;
}
