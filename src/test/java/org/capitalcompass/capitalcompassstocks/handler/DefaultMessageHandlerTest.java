package org.capitalcompass.capitalcompassstocks.handler;

import org.capitalcompass.stockservice.api.StatusMessage;
import org.capitalcompass.stockservice.handler.DefaultMessageHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class DefaultMessageHandlerTest {

    @InjectMocks
    private DefaultMessageHandler defaultMessageHandler;

    @Test
    public void handleMessagesUnknownError(CapturedOutput output) {
        StatusMessage message = new StatusMessage("unknown", "unknown_status", "unknown_message");

        Mono<Void> result = defaultMessageHandler.handleMessages(List.of(message));

        StepVerifier.create(result).verifyComplete();

        assertThat(output.getOut()).contains("Unknown Polygon messages :");
    }
}
