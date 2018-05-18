package com.sapient.retail.stock.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.retail.stock.controller.StockController;
import com.sapient.retail.stock.model.StockResponse;
import com.sapient.retail.stock.service.StockService;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class StockControllerImpl implements StockController {
    private final StockService stockService;

    /**
     * @param stockService
     */
    public StockControllerImpl(StockService stockService) {
        super();
        this.stockService = stockService;
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.controller.StockController#productStock(java.lang.String)
	 */
    @Override
	@GetMapping(value = "/stock/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<StockResponse>> productStock(@PathVariable final String productId) {
        return stockService.productStock(productId);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.controller.StockController#skuStock(java.lang.Long)
	 */
    @Override
	@GetMapping(value = "/stock/upc/{upc}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<StockResponse> skuStock(@PathVariable final Long upc) {
        return stockService.skuStock(upc);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.controller.StockController#skuStockForLocation(java.lang.Long, java.lang.Long)
	 */
    @Override
	@GetMapping(value = "/stock/upc/{upc}/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<StockResponse> skuStockForLocation(@PathVariable final Long upc, @PathVariable final Long locationId) {
        return stockService.skuStockForLocation(upc, locationId);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.controller.StockController#stockForLocation(java.lang.String, java.lang.Long)
	 */
    @Override
	@GetMapping(value = "/stock/product/{productId}/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<StockResponse>> stockForLocation(@PathVariable final String productId, @PathVariable final Long locationId) {
        return stockService.productStockForLocation(productId, locationId);
    }
}
