package org.capitalcompass.stockservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Size;

@Data
@Builder
public class TickerDetail {

    @Id
    @Generated
    private Long id;

    @Size(max = 10)
    private String symbol;

    private String name;
    private String market;
    private String primaryExchange;
    private String currencyName;
    private String type;
    private String description;
    private Long marketCap;
    private String homePageUrl;
    private Integer totalEmployees;
    private String listDate;
    private Long shareClassSharesOutstanding;
    private Long weightedSharesOutstanding;

}
