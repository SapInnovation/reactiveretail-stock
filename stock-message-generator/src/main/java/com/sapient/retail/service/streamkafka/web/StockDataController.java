package com.sapient.retail.service.streamkafka.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.retail.service.streamkafka.model.Stock;
import com.sapient.retail.service.streamkafka.service.StockDataService;

@RestController
public class StockDataController {
    private final StockDataService stockdataservice;

    public StockDataController(StockDataService stockdataservice) {
        this.stockdataservice = stockdataservice;
    }

    @PostMapping("/stockdata")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void StockData(@RequestBody String message) {
        Stock stockdata = Stock.builder().stockmessage(message)
            .build();

        stockdataservice.sendStockData(stockdata);

    }
}
