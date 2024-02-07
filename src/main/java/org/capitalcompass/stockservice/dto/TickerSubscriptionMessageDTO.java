package org.capitalcompass.stockservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TickerSubscriptionMessageDTO {
    private Set<String> symbols;
    private String userId;
}
