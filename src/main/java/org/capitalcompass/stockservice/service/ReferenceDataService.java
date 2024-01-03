package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.dto.TickerDetailDTO;
import org.capitalcompass.stockservice.dto.TickerTypesDTO;
import org.capitalcompass.stockservice.dto.TickersDTO;
import org.capitalcompass.stockservice.dto.TickersSearchConfigDTO;
import org.capitalcompass.stockservice.entity.TickerDetail;
import org.capitalcompass.stockservice.exception.TickerDetailRepositoryException;
import org.capitalcompass.stockservice.exception.TickerNotFoundException;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class ReferenceDataService {

    private final ReferenceDataClient referenceDataClient;

    private final TickerDetailRepository tickerDetailRepository;

    private final TransactionalOperator transactionalOperator;

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

    public Mono<TickerDetailDTO> getTickerDetail(String tickerSymbol) {

        Mono<TickerDetail> tickerDetailMono = getTickerDetailBySymbol(tickerSymbol)
                .switchIfEmpty(getTickerDetailFromClient(tickerSymbol));

        return tickerDetailMono.flatMap(tickerDetail -> {
            TickerDetailDTO dto = TickerDetailDTO.builder()
                    .result(tickerDetail)
                    .build();
            return Mono.just(dto);
        });
    }

    public Mono<TickerTypesDTO> getTickerTypes() {
        return referenceDataClient.getTickerTypes().map(response -> TickerTypesDTO.builder()
                .results(response.getResults())
                .build());
    }

    public Mono<Set<String>> registerTickers(Set<String> tickerSymbols) {
        return Flux.fromIterable(tickerSymbols)
                .flatMap(this::getAndSaveTickerDetail)
                .map(TickerDetail::getSymbol)
                .collect(Collectors.toSet());
    }

    private Mono<TickerDetail> getAndSaveTickerDetail(String tickerSymbol) {
        return getTickerDetailBySymbol(tickerSymbol)
                .switchIfEmpty(getTickerDetailFromClient(tickerSymbol).flatMap(this::saveTickerDetail));
    }

    private Mono<TickerDetail> getTickerDetailBySymbol(String tickerSymbol) {
        return transactionalOperator.transactional(tickerDetailRepository.findBySymbol(tickerSymbol))
                .onErrorResume(e -> {
                    log.error("Error fetching ticker detail for symbol: {}", tickerSymbol);
                    return Mono.error(new TickerDetailRepositoryException("Error accessing database for ticker symbol:" + tickerSymbol));
                });
    }

    private Mono<TickerDetail> getTickerDetailFromClient(String tickerSymbol) {
        return referenceDataClient.getTickerDetails(tickerSymbol).flatMap(response -> {
            TickerDetailResult result = response.getResults();
            if (result == null) {
                return Mono.error(new TickerNotFoundException("Not ticker detail found from client for :" + tickerSymbol));
            }
            TickerDetail tickerDetail = buildTickerDetailFromResult(response.getResults());
            return Mono.just(tickerDetail);
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

    private Mono<TickerDetail> saveTickerDetail(TickerDetail detail) {
        return transactionalOperator.transactional(this.tickerDetailRepository.save(detail))
                .onErrorResume(e -> {
                    log.error("Error saving ticker detail: {}", detail, e);
                    return Mono.error(new TickerDetailRepositoryException("Error accessing database to save Ticker Detail :" + detail));
                });
    }

    private TickerDetail buildTickerDetailFromResult(TickerDetailResult result) {
        return TickerDetail.builder()
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
    }
}
