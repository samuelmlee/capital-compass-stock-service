package org.capitalcompass.stockservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SubscriptionMessageDTO {
    private Set<String> symbols;
}
