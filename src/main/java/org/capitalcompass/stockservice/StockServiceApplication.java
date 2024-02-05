package org.capitalcompass.stockservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class StockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockServiceApplication.class, args);
    }

}
