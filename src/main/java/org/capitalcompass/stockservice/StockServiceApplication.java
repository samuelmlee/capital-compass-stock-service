package org.capitalcompass.stockservice;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.stockservice.client.WebSocketDataClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class StockServiceApplication implements ApplicationRunner {

    private final WebSocketDataClient webSocketDataClient;

    public static void main(String[] args) {
        SpringApplication.run(StockServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        webSocketDataClient.connectAndSubscribe().subscribe();
    }
}
