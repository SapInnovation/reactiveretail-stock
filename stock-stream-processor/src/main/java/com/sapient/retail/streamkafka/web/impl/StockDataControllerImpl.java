package com.sapient.retail.streamkafka.web.impl;

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

    /* (non-Javadoc)
	 * @see com.sapient.retail.streamkafka.web.StockDataController#StockData(com.sapient.retail.stock.common.model.Stock)
	 */
    @Override
	@RequestMapping(value="/stockdata", method = RequestMethod.POST, 
    		consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void StockData(@RequestBody RetailStock productStock) {

        stockdataservice.sendStockDataToKafkaTopic(productStock);
    }
}
