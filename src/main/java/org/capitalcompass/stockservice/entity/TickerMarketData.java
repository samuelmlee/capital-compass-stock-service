package org.capitalcompass.stockservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.sql.Timestamp;

@Data
@Builder
public class TickerMarketData {

    @Id
    @Generated
    private Long id;

    private Timestamp updatedTimestamp;

    private Long marketCap;
    private Long shareClassSharesOutstanding;
    private Long weightedSharesOutstanding;

    @Column("ticker_detail_id")
    private Long tickerDetailId;

}
