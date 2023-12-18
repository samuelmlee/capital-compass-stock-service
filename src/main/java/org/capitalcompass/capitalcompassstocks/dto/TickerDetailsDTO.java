package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.capitalcompassstocks.api.TickerDetailsResult;

@Data
@Builder
public class TickerDetailsDTO {
    private TickerDetailsResult result;
}
