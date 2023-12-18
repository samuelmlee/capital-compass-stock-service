package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.capitalcompassstocks.api.TypeResult;

import java.util.ArrayList;

@Data
@Builder
public class TickerTypesDTO {
    private ArrayList<TypeResult> results;
}
