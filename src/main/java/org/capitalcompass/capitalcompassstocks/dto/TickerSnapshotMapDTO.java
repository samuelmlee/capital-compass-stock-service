package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class TickerSnapshotMapDTO {
    Map<String, TickerSnapshotDTO> tickers;
}
