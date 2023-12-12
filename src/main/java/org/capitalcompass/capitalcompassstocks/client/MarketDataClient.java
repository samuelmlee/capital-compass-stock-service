package org.capitalcompass.capitalcompassstocks.client;


import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassstocks.model.TickerSnapShotResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MarketDataClient {

    private final WebClient webClient;

    private final String snapshotsUri = "/v2/snapshot/locale/us/markets/stocks";

    public Mono<TickerSnapShotResponse> getTickerSnapShots(String tickerSymbol) {
        return webClient.get().uri(snapshotsUri + "/tickers/{symbol}", tickerSymbol)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerSnapShotResponse.class);
    }


}
