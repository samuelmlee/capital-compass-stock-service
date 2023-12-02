package org.capitalcompass.capitalcompassstocks.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TickerTypesResponse {
    private ArrayList<TypeResult> results;
    private int count;
}
