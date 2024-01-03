package org.capitalcompass.stockservice.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@Builder
public class TickersSearchConfigDTO {

    @Size(max = 5)
    private String symbol;

    private String searchTerm;

    @Min(value = 10)
    @Max(value = 1000)
    private Integer resultsCount;

    // TODO: cache ticker types, update cache on start
    private String type;

}
