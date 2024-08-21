package org.capitalcompass.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capitalcompass.stockservice.api.TickerNewsResult;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TickerNewsDTO {
    List<TickerNewsResult> results;
    private String nextCursor;

}
