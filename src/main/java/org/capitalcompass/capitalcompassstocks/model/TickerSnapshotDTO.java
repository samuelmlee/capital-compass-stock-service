package org.capitalcompass.capitalcompassstocks.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TickerSnapshotDTO {
    TickerSnapshot ticker;
}
