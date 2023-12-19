package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.capitalcompassstocks.entity.TickerDetail;

@Data
@Builder
public class TickerDetailDTO {
    private TickerDetail result;
}
