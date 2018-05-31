package com.sapient.retail.streamkafka.web;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.streamkafka.service.StockDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StockConsumerController {
    private final StockDataService<RetailStock> stockDataService;

    public StockConsumerController(final StockDataService<RetailStock> stockDataService) {
        this.stockDataService = stockDataService;
    }

    /**
     * Rest API endpoint for stock updates.
     *
     * @param productStock the stock update
     */
    @RequestMapping(value = "/stockdata", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateStock(@Valid @RequestBody final RetailStock productStock) {
        stockDataService.sendStockDataToKafkaTopic(productStock);
    }
}
