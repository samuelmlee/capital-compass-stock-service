package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class TickersResponseDTO {

    private ArrayList<TickersResult> results;

    private String nextCursor;

}
