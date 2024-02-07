package org.capitalcompass.stockservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StatusMessage extends PolygonMessage {

    private String status;

    private String message;

    public StatusMessage(String event, String status, String message) {
        super(event);
        this.status = status;
        this.message = message;
    }
}
