package com.sapient.retail.service.streamkafka.web;

import com.sapient.retail.service.streamkafka.model.StockData;
import com.sapient.retail.service.streamkafka.service.StockDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockDataController {
    private final StockDataService stockdataservice;

    public StockDataController(StockDataService stockdataservice) {
        this.stockdataservice = stockdataservice;
    }

    @GetMapping("/stockdata")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void StockData(@RequestParam("message") String message) {
        StockData stockdata = StockData.builder().stockdatamessage(message)
            .build();

        stockdataservice.sendStockData(stockdata);

    }
}
