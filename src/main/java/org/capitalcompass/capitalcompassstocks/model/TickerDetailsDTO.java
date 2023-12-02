package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TickerDetailsDTO {
    private TickersResult results;
}
