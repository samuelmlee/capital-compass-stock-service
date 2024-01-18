package org.capitalcompass.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.client.ReferenceDataClient;
import org.capitalcompass.stockservice.dto.*;
import org.capitalcompass.stockservice.entity.TickerDetail;
import org.capitalcompass.stockservice.exception.TickerDetailRepositoryException;
import org.capitalcompass.stockservice.exception.TickerNotFoundException;
import org.capitalcompass.stockservice.repository.TickerDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service class for managing reference data for stock tickers.
 * It interacts with the ReferenceDataClient for fetching ticker data from Polygon.io and handles
 * fetching and saving ticker details.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ReferenceDataService {

    private final ReferenceDataClient referenceDataClient;

    private final TickerDetailRepository tickerDetailRepository;

    /**
     * Retrieves a list of tickers based on the specified search configuration.
     *
     * @param config The TickersSearchConfigDTO configuration for ticker search.
     * @return A Mono of TickersDTO containing the list of tickers and a cursor for pagination.
     */
    public Mono<TickersDTO> getTickersByConfig(TickersSearchConfigDTO config) {
        return referenceDataClient.getTickersByConfig(config).flatMap(response -> {
            String nextCursor = getNextCursorFromResponse(response.getNextUrl());

            TickersDTO dto = TickersDTO.builder()
                    .results(response.getResults())
                    .nextCursor(nextCursor)
                    .build();
            return Mono.just(dto);
        });
    }

    /**
     * Retrieves a list of tickers using a pagination cursor.
     *
     * @param cursor The pagination cursor to fetch the next page of tickers.
     * @return A Mono of TickersDTO containing the list of tickers and the next cursor.
     */
    public Mono<TickersDTO> getTickersByCursor(String cursor) {

        return referenceDataClient.getTickersByCursor(cursor).flatMap(response -> {
            String nextCursor = getNextCursorFromResponse(response.getNextUrl());

            TickersDTO dto = TickersDTO.builder()
                    .results(response.getResults())
                    .nextCursor(nextCursor)
                    .build();
            return Mono.just(dto);
        });
    }

    /**
     * Fetches details for a specific ticker symbol.
     * It first attempts to retrieve the ticker detail from the local repository,
     * and if not found, fetches it from the external client.
     *
     * @param tickerSymbol The symbol of the ticker to fetch details for.
     * @return A Mono of TickerDetailDTO containing the ticker's detail.
     */
    public Mono<TickerDetailDTO> getTickerDetail(String tickerSymbol) {

        Mono<TickerDetail> tickerDetailMono = getTickerDetailFromRepo(tickerSymbol)
                .switchIfEmpty(getTickerDetailFromClient(tickerSymbol));

        return tickerDetailMono.flatMap(tickerDetail -> {
            TickerDetailDTO dto = TickerDetailDTO.builder()
                    .result(tickerDetail)
                    .build();
            return Mono.just(dto);
        });
    }

    /**
     * Retrieves the available types of tickers.
     *
     * @return A Mono of TickerTypesDTO containing the list of ticker types.
     */
    public Mono<TickerTypesDTO> getTickerTypes() {
        return referenceDataClient.getTickerTypes().map(response -> TickerTypesDTO.builder()
                .results(response.getResults())
                .build());
    }

    /**
     * Retrieves news articles associated with a given ticker symbol and wraps the response
     * in a {@link TickerNewsDTO}.
     * <p>
     * This method calls the {@link ReferenceDataClient#getTickerNews(String)} method to fetch news
     * related to the provided ticker symbol. It then processes the response to extract the next cursor
     * and encapsulates the result along with the cursor in a {@link TickerNewsDTO}.
     * <p>
     * The method uses a flatMap operation to transform the data received from the
     * {@link ReferenceDataClient} into a {@link Mono} of {@link TickerNewsDTO}, ensuring that
     * the asynchronous nature of the operation is maintained.
     *
     * @param tickerSymbol The ticker symbol for which the news is to be retrieved. This should be a
     *                     non-null and valid string representing a ticker symbol.
     * @return A {@link Mono} of {@link TickerNewsDTO} containing the news articles and the next cursor
     * for pagination purposes. In case of an error in the underlying client call, the error
     * will be propagated downstream.
     */
    public Mono<TickerNewsDTO> getTickerNews(String tickerSymbol) {
        return referenceDataClient.getTickerNews(tickerSymbol).flatMap(response -> {
            String nextCursor = getNextCursorFromResponse(response.getNextUrl());

            TickerNewsDTO dto = TickerNewsDTO.builder()
                    .results(response.getResults())
                    .nextCursor(nextCursor)
                    .build();
            return Mono.just(dto);
        });
    }

    /**
     * Registers a set of ticker symbols by fetching and saving their details.
     *
     * @param tickerSymbols The set of ticker symbols to register.
     * @return A Mono of a Set containing the symbols of successfully registered tick
     * ers.
     */
    @Transactional
    public Mono<Set<String>> registerTickers(Set<String> tickerSymbols) {
        return Flux.fromIterable(tickerSymbols)
                .flatMap(this::getAndSaveTickerDetail)
                .map(TickerDetail::getSymbol)
                .collect(Collectors.toSet());
    }

    /**
     * Fetches and saves the ticker detail for the given ticker symbol.
     * It first attempts to retrieve the ticker detail from tickerDetailRepository,
     * and if not found, fetches it from the external client and saves it.
     *
     * @param tickerSymbol The symbol of the ticker to fetch and save.
     * @return A Mono of TickerDetail representing the saved ticker detail.
     */
    private Mono<TickerDetail> getAndSaveTickerDetail(String tickerSymbol) {
        return getTickerDetailFromRepo(tickerSymbol)
                .switchIfEmpty(getTickerDetailFromClient(tickerSymbol).flatMap(this::saveTickerDetail));
    }

    /**
     * Retrieves the ticker detail for a given symbol from tickerDetailRepository.
     *
     * @param tickerSymbol The symbol of the ticker to retrieve.
     * @return A Mono of TickerDetail if found, or Mono.empty() if not found.
     */
    private Mono<TickerDetail> getTickerDetailFromRepo(String tickerSymbol) {
        return tickerDetailRepository.findBySymbol(tickerSymbol)
                .onErrorResume(e -> {
                    log.error("Error fetching ticker detail for symbol: {}", tickerSymbol);
                    return Mono.error(new TickerDetailRepositoryException("Error accessing database for ticker symbol:" + tickerSymbol));
                });
    }

    /**
     * Fetches the ticker detail for a given symbol from referenceDataClient.
     *
     * @param tickerSymbol The symbol of the ticker to fetch.
     * @return A Mono of TickerDetail containing the fetched ticker detail.
     */
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

    /**
     * Extracts the cursor from the response URL for pagination purposes.
     *
     * @param uri The URI string to extract the cursor from.
     * @return The extracted cursor or an empty string if not found.
     */
    private String getNextCursorFromResponse(String uri) {
        try {
            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
            String cursor = parameters.getFirst("cursor");
            return cursor != null ? cursor : "";
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    /**
     * Saves the given TickerDetail entity to the repository.
     *
     * @param detail The TickerDetail entity to save.
     * @return A Mono of TickerDetail representing the saved entity.
     * typescript
     * Copy code
     */
    private Mono<TickerDetail> saveTickerDetail(TickerDetail detail) {
        return this.tickerDetailRepository.save(detail)
                .onErrorResume(e -> {
                    log.error("Error saving ticker detail: {}", detail, e);
                    return Mono.error(new TickerDetailRepositoryException("Error accessing database to save Ticker Detail :" + detail));
                });
    }

    /**
     * Constructs a TickerDetail entity from the TickerDetailResult.
     *
     * @param result The TickerDetailResult to convert.
     * @return A TickerDetail entity constructed from the given result.
     */
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
