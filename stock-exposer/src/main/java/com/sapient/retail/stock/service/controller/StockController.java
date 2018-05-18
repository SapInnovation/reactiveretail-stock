package com.sapient.retail.stock.service.controller;

import com.sapient.retail.stock.service.model.StockResponse;
import com.sapient.retail.stock.service.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class StockController {
    private final StockService stockService;

    /**
     * @param stockService
     */
    public StockController(StockService stockService) {
        super();
        this.stockService = stockService;
    }

    @GetMapping(value = "/stock/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<StockResponse>> productStock(@PathVariable final String productId) {
        return stockService.productStock(productId);
    }

    @GetMapping(value = "/stock/upc/{upc}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<StockResponse> skuStock(@PathVariable final Long upc) {
        return stockService.skuStock(upc);
    }

    @GetMapping(value = "/stock/upc/{upc}/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<StockResponse> skuStockForLocation(@PathVariable final Long upc, @PathVariable final Long locationId) {
        return stockService.skuStockForLocation(upc, locationId);
    }

    @GetMapping(value = "/stock/product/{productId}/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<StockResponse>> stockForLocation(@PathVariable final String productId, @PathVariable final Long locationId) {
        return stockService.productStockForLocation(productId, locationId);
    }
}
