package com.sapient.retail.stock.service.controller;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.service.model.StockResponse;
import com.sapient.retail.stock.service.service.StockStreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StockStreamController {
    private final StockStreamService stockStreamService;

    public StockStreamController(final StockStreamService streamService) {
        this.stockStreamService = streamService;
    }

    @GetMapping(value = "/stream/stock/product/{productId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockResponse> stockStream(@PathVariable final String productId) {
        return stockStreamService.stockStream(productId);
    }

    @GetMapping(value = "/stream/stock/upc/{upc}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockResponse> skuStockStream(@PathVariable final Long upc) {
        return stockStreamService.skuStockStream(upc);
    }

    @GetMapping(value = "/stream/stock/product/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Stock> stockStream() {
        return stockStreamService.allStockStream();
    }

}
