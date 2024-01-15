package org.capitalcompass.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capitalcompass.stockservice.api.TickerResult;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TickersDTO {

    private List<TickerResult> results;

    private String nextCursor;

}
