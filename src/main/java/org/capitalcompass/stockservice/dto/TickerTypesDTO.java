package org.capitalcompass.stockservice.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.stockservice.api.TypeResult;

import java.util.ArrayList;

@Data
@Builder
public class TickerTypesDTO {
    private ArrayList<TypeResult> results;
}
