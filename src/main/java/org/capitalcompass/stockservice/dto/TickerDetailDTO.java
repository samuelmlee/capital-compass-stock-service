package org.capitalcompass.stockservice.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.stockservice.entity.TickerDetail;

@Data
@Builder
public class TickerDetailDTO {
    private TickerDetail result;
}
