package com.sapient.retail.service.streamkafka.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.retail.model.ProductStock;
import com.sapient.retail.service.streamkafka.service.StockDataService;

@RestController
public class StockDataController {
    private final StockDataService stockdataservice;

    public StockDataController(StockDataService stockdataservice) {
        this.stockdataservice = stockdataservice;
    }

    @PostMapping("/stockdata")
    @RequestMapping(value="/stockdata", method = RequestMethod.POST, 
    		consumes = "application/json; charset=utf-8")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void StockData(@RequestBody ProductStock productStock) {
        /*ProductStock stockdata = ProductStock.builder()
        		.productId(stockInfo.getProductId())
        		.stock(stockInfo.getStock())
        		.upc(stockInfo.getUpc())
        		.stock(stockInfo.getStock())
            .build();*/

        stockdataservice.sendStockData(productStock);
    }
}
