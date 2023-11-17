package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TickersSearchConfig {
    private String cursor;

    private String tickerSymbol;

    private String searchTerm;

    private Integer resultsCount = 50;

    private Boolean active = true;

}
