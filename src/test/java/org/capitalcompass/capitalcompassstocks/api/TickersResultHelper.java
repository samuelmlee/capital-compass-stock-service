package org.capitalcompass.capitalcompassstocks.api;

public class TickersResultHelper {
    public static TickerResult createTickerResult(String symbol, String name, String market, String currencyName, String primaryExchange) {
        TickerResult tickerResult = new TickerResult();
        tickerResult.setSymbol(symbol);
        tickerResult.setName(name);
        tickerResult.setMarket(market);
        tickerResult.setCurrencyName(currencyName);
        tickerResult.setPrimaryExchange(primaryExchange);
        return tickerResult;
    }
}
