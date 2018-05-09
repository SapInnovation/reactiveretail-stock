package com.sapient.retail.service.streamkafka.web;

import com.sapient.retail.service.streamkafka.model.Greetings;
import com.sapient.retail.service.streamkafka.model.StockData;
import com.sapient.retail.service.streamkafka.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockDataController {
    private final StockService stockService;

    public StockDataController(StockService greetingsService) {
        this.greetingsService = greetingsService;
    }

    @GetMapping("/stock")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void stockdata(@RequestParam("message") String message) {
        StockData stockdata = StockData.builder()
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();

        stockService.sendGreeting(stockdata);
    }
}
