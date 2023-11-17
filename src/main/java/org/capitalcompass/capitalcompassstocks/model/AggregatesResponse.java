package org.capitalcompass.capitalcompassstocks.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class AggregatesResponse {
    public String ticker;
    public int queryCount;
    public int resultsCount;
    public boolean adjusted;
    public ArrayList<AggregatesResult> results;
    public String status;
    public String request_id;
    public int count;
}
