package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class TickerTypesResponseDTO {
    private ArrayList<TypeResult> results;
}
