package org.capitalcompass.stockservice.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.stockservice.api.TickerResult;

import java.util.ArrayList;

@Data
@Builder
public class TickersDTO {

    private ArrayList<TickerResult> results;

    private String nextCursor;

}
