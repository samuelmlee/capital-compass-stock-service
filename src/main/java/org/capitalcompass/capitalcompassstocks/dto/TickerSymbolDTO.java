package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class TickerSymbolDTO {

    @NotNull
    @Size(min = 1, max = 5, message = "Ticker symbol must be between 1 and 5 characters")
    @Pattern(regexp = "^[A-Z]+$", message = "Ticker symbol must only be composed of letters")
    private String symbol;
}
