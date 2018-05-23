package com.sapient.retail.stock.controller;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.model.StockResponse;

import com.sapient.retail.stock.service.StreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StreamController {
    private final StreamService streamService;

    public StreamController(final StreamService streamService) {
        this.streamService = streamService;
    }


	@GetMapping(value = "/stream/stock/product/{productId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockResponse> stockStream(@PathVariable final String productId) {
        return streamService.stockStream(productId);
    }

    @GetMapping(value = "/stream/stock/upc/{upc}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockResponse> skuStockStream(@PathVariable final Long upc) {
        return streamService.skuStockStream(upc);
    }

    @GetMapping(value = "/stream/stock/product/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Stock> stockStream() {
        return streamService.allStockStream();
    }

}
