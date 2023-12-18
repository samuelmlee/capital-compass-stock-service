package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.capitalcompassstocks.api.TickerDetailResult;

@Data
@Builder
public class TickerDetailsDTO {
    private TickerDetailResult result;
}
