package com.sapient.retail.streamkafka.web.impl;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.streamkafka.service.StockDataService;
import com.sapient.retail.streamkafka.web.StockDataController;

@RestController
public class StockDataControllerImpl implements StockDataController<RetailStock> {
    private final StockDataService<RetailStock> stockdataservice;

    public StockDataControllerImpl(StockDataService<RetailStock> stockdataservice) {
        this.stockdataservice = stockdataservice;
    }

    /**
     * Rest API endpoint for stock updates.
     *
     * @param productStock the stock update
     */
    @Override
    @RequestMapping(value = "/stockdata", method = RequestMethod.POST,
            consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateStock(@Valid @RequestBody RetailStock productStock) {
        stockdataservice.sendStockDataToKafkaTopic(productStock);
    }
}
