package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Builder
@Getter
@Setter
public class TickerTypesResponseDTO {
    private ArrayList<TypeResult> results;
}
