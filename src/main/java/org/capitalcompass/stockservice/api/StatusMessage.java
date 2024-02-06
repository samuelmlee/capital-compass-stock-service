package org.capitalcompass.stockservice.api;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
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
