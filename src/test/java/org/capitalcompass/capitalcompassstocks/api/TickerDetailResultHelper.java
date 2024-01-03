package org.capitalcompass.capitalcompassstocks.api;

import org.capitalcompass.stockservice.api.TickerDetailResult;
import org.capitalcompass.stockservice.entity.TickerDetail;

public class TickerDetailResultHelper {
    public static TickerDetailResult createTickerDetailResult() {
        TickerDetailResult tickerDetailResult = new TickerDetailResult();
        tickerDetailResult.setSymbol("TSLA");
        tickerDetailResult.setName("Tesla, Inc.");
        tickerDetailResult.setMarket("NASDAQ");
        tickerDetailResult.setPrimaryExchange("NASDAQ");
        tickerDetailResult.setCurrencyName("USD");
        tickerDetailResult.setType("Equity");
        tickerDetailResult.setDescription("Tesla, Inc. designs, develops, manufactures, leases, and sells electric vehicles, and energy generation and storage systems in the United States, China, and internationally.");
        tickerDetailResult.setMarketCap(800000000000L);
        tickerDetailResult.setHomePageUrl("https://www.tesla.com/");
        tickerDetailResult.setTotalEmployees(48016);
        tickerDetailResult.setListDate("2010-06-29");
        tickerDetailResult.setShareClassSharesOutstanding(1000000000L);
        tickerDetailResult.setWeightedSharesOutstanding(950000000L);

        return tickerDetailResult;
    }

    public static TickerDetail buildTickerDetailFromResult(TickerDetailResult result) {
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
