package org.capitalcompass.stockservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Builder
public class TickerMarketData {

    @Id
    @Generated
    private Long id;

    private Date date;

    private Long marketCap;
    private Long shareClassSharesOutstanding;
    private Long weightedSharesOutstanding;

    @ManyToOne
    private TickerDetail tickerDetail;

}
