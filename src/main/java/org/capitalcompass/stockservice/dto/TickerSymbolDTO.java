package org.capitalcompass.stockservice.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class TickerSymbolDTO {

    @NotNull
    @Size(min = 1, max = 5, message = "Ticker symbol must be between 1 and 5 characters")
    @Pattern(regexp = "^[A-Z]+$", message = "Ticker symbol must only be composed of letters")
    private String symbol;
}
