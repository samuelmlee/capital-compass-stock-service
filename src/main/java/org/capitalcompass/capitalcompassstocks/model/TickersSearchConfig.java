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

    private String ticker;

    private String searchTerm;

    private Integer resultsCount = 100;

    private String type;

}
