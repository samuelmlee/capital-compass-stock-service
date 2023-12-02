package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@Builder
public class TickersResponseDTO {

    private ArrayList<TickersResult> results;

    private String nextCursor;

}
