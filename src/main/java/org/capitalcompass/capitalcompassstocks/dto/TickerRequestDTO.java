package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Data;

import java.util.Set;

@Data
public class TickerRequestDTO {

    private Set<String> symbols;
}
