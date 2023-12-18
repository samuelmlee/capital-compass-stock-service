package org.capitalcompass.capitalcompassstocks.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.api.TickerResult;
import org.capitalcompass.capitalcompassstocks.client.ReferenceDataClient;
import org.capitalcompass.capitalcompassstocks.dto.TickerDetailsDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickerTypesDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersSearchConfigDTO;
import org.capitalcompass.capitalcompassstocks.entity.Ticker;
import org.capitalcompass.capitalcompassstocks.repository.TickerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ReferenceDataService {

    private final ReferenceDataClient referenceDataClient;

    private final TickerRepository tickerRepository;

    public Mono<TickersDTO> getTickers(TickersSearchConfigDTO config) {
        return referenceDataClient.getTickers(config).flatMap(response -> {
            String nextCursor = getCursorFromTickersResponse(response.getNextUrl());

            TickersDTO dto = TickersDTO.builder()
                    .results(response.getResults())
                    .nextCursor(nextCursor)
                    .build();
            return Mono.just(dto);
        });
    }

    public Mono<TickerDetailsDTO> getTickerDetails(String tickerSymbol) {
        return referenceDataClient.getTickerDetails(tickerSymbol).flatMap(response -> {
            TickerDetailsDTO dto = TickerDetailsDTO.builder()
                    .result(response.getResults())
                    .build();
            return Mono.just(dto);
        });
    }

    public Mono<TickersDTO> getTickersByCursor(String cursor) {

        return referenceDataClient.getTickersByCursor(cursor).flatMap(response -> {
            String nextCursor = getCursorFromTickersResponse(response.getNextUrl());

            TickersDTO dto = TickersDTO.builder()
                    .results(response.getResults())
                    .nextCursor(nextCursor)
                    .build();
            return Mono.just(dto);
        });
    }

    public Mono<TickerTypesDTO> getTickerTypes() {
        return referenceDataClient.getTickerTypes().flatMap(response -> {
            TickerTypesDTO dto = TickerTypesDTO.builder()
                    .results(response.getResults())
                    .build();
            return Mono.just(dto);

        });
    }

    private String getCursorFromTickersResponse(String uri) {
        try {
            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
            String cursor = parameters.getFirst("cursor");
            return cursor != null ? cursor : "";
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    @Transactional
    public Mono<Boolean> registerTicker(TickersSearchConfigDTO config) {
        return referenceDataClient.getTickers(config).flatMap(response -> {
            List<TickerResult> results = response.getResults();

            if (results.isEmpty() || !Objects.equals(results.get(0).getSymbol(), config.getSymbol())) {
                return Mono.just(false);
            }

            return saveTickerResult(results.get(0)).thenReturn(true);
        });

    }

    private Mono<Ticker> saveTickerResult(TickerResult result) {
        Ticker tickerEntity = Ticker.builder()
                .symbol(result.getSymbol())
                .name(result.getName())
                .market(result.getMarket())
                .primaryExchange(result.getPrimaryExchange())
                .currencyName(result.getCurrencyName())
                .build();

        return this.tickerRepository.save(tickerEntity);
    }
}
