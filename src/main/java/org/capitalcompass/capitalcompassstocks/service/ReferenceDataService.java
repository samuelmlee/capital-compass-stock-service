package org.capitalcompass.capitalcompassstocks.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.api.TickerDetailResult;
import org.capitalcompass.capitalcompassstocks.client.ReferenceDataClient;
import org.capitalcompass.capitalcompassstocks.dto.TickerDetailsDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickerTypesDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersDTO;
import org.capitalcompass.capitalcompassstocks.dto.TickersSearchConfigDTO;
import org.capitalcompass.capitalcompassstocks.entity.TickerDetail;
import org.capitalcompass.capitalcompassstocks.repository.TickerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

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
                    .result(response.getResult())
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
    public Mono<Boolean> registerTicker(String tickerSymbol) {
        return referenceDataClient.getTickerDetails(tickerSymbol).flatMap(response -> {
            TickerDetailResult tickerDetail = response.getResult();

            if (tickerDetail == null || !Objects.equals(tickerDetail.getSymbol(), tickerSymbol)) {
                return Mono.just(false);
            }

            return saveTickerResult(tickerDetail).thenReturn(true);
        });

    }

    private Mono<TickerDetail> saveTickerResult(TickerDetailResult result) {
        TickerDetail detail = TickerDetail.builder()
                .symbol(result.getSymbol())
                .name(result.getName())
                .market(result.getMarket())
                .primaryExchange(result.getPrimaryExchange())
                .currencyName(result.getCurrencyName())
                .type(result.getType())
                .description(result.getDescription())
                .marketCap(result.getMarketCap())
                .homePageUrl(result.getHomePageUrl())
                .totalEmployees(result.getTotalEmployees())
                .listDate(result.getListDate())
                .shareClassSharesOutstanding(result.getShareClassSharesOutstanding())
                .weightedSharesOutstanding(result.getWeightedSharesOutstanding())
                .build();

        return this.tickerRepository.save(detail);
    }
}
