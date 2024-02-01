package org.capitalcompass.stockservice.api;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionMessage {
    private List<String> symbols;
}
