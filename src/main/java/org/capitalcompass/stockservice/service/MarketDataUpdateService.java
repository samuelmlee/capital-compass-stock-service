package org.capitalcompass.stockservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MarketDataUpdateService {

    @Scheduled(cron = "@daily")
    public void updateTickerMarketData() throws InterruptedException {

    }
}
