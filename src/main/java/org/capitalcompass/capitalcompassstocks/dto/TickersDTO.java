package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Builder;
import lombok.Data;
import org.capitalcompass.capitalcompassstocks.api.TickerResult;

import java.util.ArrayList;

@Data
@Builder
public class TickersDTO {

    private ArrayList<TickerResult> results;

    private String nextCursor;

}
