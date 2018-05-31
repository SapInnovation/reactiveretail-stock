package com.sapient.retail.stock.controller;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.model.impl.StockResponse;
import com.sapient.retail.stock.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class StockController {
    private final StockService<RetailStock, StockResponse> stockService;

    /**
     * Dependency injection based constructor.
     *
     * @param stockService service
     */
    public StockController(final StockService<RetailStock, StockResponse> stockService) {
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
    public Mono<StockResponse> skuStock(@Valid @PathVariable final Long upc) {
        return stockService.skuStock(upc);
    }

    @GetMapping(value = "/stock/upc/{upc}/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<StockResponse> skuStockForLocation(@Valid @PathVariable final Long upc,
                                                   @Valid @PathVariable final Long locationId) {
        return stockService.skuStockForLocation(upc, locationId);
    }

    @GetMapping(value = "/stock/product/{productId}/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<StockResponse>> stockForLocation(@Valid @PathVariable final String productId,
                                                      @Valid @PathVariable final Long locationId) {
        return stockService.productStockForLocation(productId, locationId);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> home() {
        return Mono.just("Reactive Retail - Stock Service");
    }
}
