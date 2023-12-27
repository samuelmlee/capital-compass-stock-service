package org.capitalcompass.capitalcompassstocks.service;

import org.capitalcompass.capitalcompassstocks.api.TickersResponse;
import org.capitalcompass.capitalcompassstocks.client.ReferenceDataClient;
import org.capitalcompass.capitalcompassstocks.dto.TickersDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersSearchConfigDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReferenceDataServiceTest {

    @Mock
    private ReferenceDataClient referenceDataClient;

    @InjectMocks
    private ReferenceDataService referenceDataService;

    @Test
    public void getTickersTest() {
        TickersSearchConfigDTO mockConfig = new TickersSearchConfigDTO();

        TickersResponse mockResponse = new TickersResponse();


        when(referenceDataClient.getTickers(mockConfig)).thenReturn(Mono.just(mockResponse));

        Mono<TickersDTO> result = referenceDataService.getTickers(mockConfig);

        StepVerifier.create(result)
                .expectNextMatches(tickersDTO ->
                        tickersDTO.getResults().equals(mockResponse.getResults()) &&
                                tickersDTO.getNextCursor().equals("nextCursor"))
                .verifyComplete();
    }
}