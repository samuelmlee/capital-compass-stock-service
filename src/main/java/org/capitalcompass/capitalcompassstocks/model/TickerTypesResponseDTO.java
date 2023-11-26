package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;

import java.util.ArrayList;

@Builder
public class TickerTypesResponseDTO {
    public ArrayList<TypeResult> results;
}
