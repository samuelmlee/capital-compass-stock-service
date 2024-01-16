package org.capitalcompass.stockservice.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.stockservice.api.TickerTypeResult;

import java.util.List;

@Data
@Builder
public class TickerTypesDTO {
    private List<TickerTypeResult> results;
}
