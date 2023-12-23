package org.capitalcompass.capitalcompassstocks.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class TickerRequestDTO {

    @NotEmpty(message = "Set of symbols cannot be empty")
    private Set<String> symbols;
}
