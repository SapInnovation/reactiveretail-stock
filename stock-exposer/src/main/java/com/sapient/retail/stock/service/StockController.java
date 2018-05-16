package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.service.service.StreamService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StockController {
    private final StreamService streamService;

    public StockController(final StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping(value = "/stream/product/{productId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Stock> stockStream(@PathVariable final String productId) {
        return streamService.stockStream(productId);
    }

    @GetMapping(value = "/stock/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Stock> productStock(@PathVariable final String productId) {
        return streamService.productStock(productId);
    }
}
