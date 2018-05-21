package com.sapient.retail.stock.controller.impl;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.controller.StockStreamController;
import com.sapient.retail.stock.model.StockResponse;
import com.sapient.retail.stock.service.StockStreamService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class StockStreamControllerImpl implements StockStreamController {
    private final StockStreamService stockStreamService;

    public StockStreamControllerImpl(final StockStreamService streamService) {
        this.stockStreamService = streamService;
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.controller.StockStreamController#stockStream(java.lang.String)
	 */
    @Override
	@GetMapping(value = "/stream/stock/product/{productId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockResponse> stockStream(@PathVariable final String productId) {
        return stockStreamService.stockStream(productId);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.controller.StockStreamController#skuStockStream(java.lang.Long)
	 */
    @Override
	@GetMapping(value = "/stream/stock/upc/{upc}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockResponse> skuStockStream(@PathVariable final Long upc) {
        return stockStreamService.skuStockStream(upc);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.controller.StockStreamController#stockStream()
	 */
    @Override
	@GetMapping(value = "/stream/stock/product/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<RetailStock> stockStream() {
        return stockStreamService.allStockStream();
    }

}
