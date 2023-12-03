package org.capitalcompass.capitalcompassstocks.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TickerTypesResponse {
    private ArrayList<TypeResult> results;
    private int count;
}
