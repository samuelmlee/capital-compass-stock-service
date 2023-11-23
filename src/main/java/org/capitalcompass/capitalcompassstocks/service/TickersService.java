package org.capitalcompass.capitalcompassstocks.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.client.ReferenceDataClient;
import org.capitalcompass.capitalcompassstocks.model.TickersResponseDTO;
import org.capitalcompass.capitalcompassstocks.model.TickersSearchConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class TickersService {

    private final ReferenceDataClient referenceDataClient;

    public Mono<TickersResponseDTO> getTickers(TickersSearchConfig config) {
        return referenceDataClient.getTickers(config).flatMap(response -> {
            String nextCursor = getCursorFromTickersResponse(response.nextUrl);

            TickersResponseDTO dto = TickersResponseDTO.builder()
                    .results(response.results)
                    .nextCursor(nextCursor)
                    .build();
            return Mono.just(dto);
        });
    }

    public Mono<TickersResponseDTO> getTickersByCursor(String cursor) {

        return referenceDataClient.getTickersByCursor(cursor).flatMap(response -> {
            String nextCursor = getCursorFromTickersResponse(response.nextUrl);

            TickersResponseDTO dto = TickersResponseDTO.builder()
                    .results(response.results)
                    .nextCursor(nextCursor)
                    .build();
            return Mono.just(dto);
        });
    }

    private String getCursorFromTickersResponse(String uri) {
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
        return parameters.getFirst("cursor");
    }
}
