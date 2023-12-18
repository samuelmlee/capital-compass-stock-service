package org.capitalcompass.capitalcompassstocks.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Size;

@Data
@Builder
public class Ticker {

    @Id
    @Generated
    private Long id;

    @Size(max = 10)
    private String symbol;
    private String name;
    private String market;
    private String currencyName;
    private String primaryExchange;

}
