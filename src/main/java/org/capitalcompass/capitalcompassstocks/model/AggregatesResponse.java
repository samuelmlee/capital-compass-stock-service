package org.capitalcompass.capitalcompassstocks.model;

import java.util.ArrayList;

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
