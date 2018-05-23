package com.sapient.retail.streamkafka.web;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.streamkafka.service.impl.StockDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StockDataController {
    private final StockDataService stockdataservice;

    public StockDataController(StockDataService stockdataservice) {
        this.stockdataservice = stockdataservice;
    }

    /**
     * Rest API endpoint for stock updates.
     *
     * @param productStock the stock update
     */
    @RequestMapping(value = "/stockdata", method = RequestMethod.POST,
            consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateStock(@Valid @RequestBody Stock productStock) {
        stockdataservice.sendStockDataToKafkaTopic(productStock);
    }
}
