package com.sapient.retail.stock.controller;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.model.impl.StockResponse;
import com.sapient.retail.stock.service.StreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class StreamController {
    private final StreamService<RetailStock, StockResponse> streamService;

    public StreamController(final StreamService<RetailStock, StockResponse> streamService) {
        this.streamService = streamService;
    }

    @PostConstruct
    public void initializeMongoStreams() {
        streamService.registerStream();
    }

    @GetMapping(value = "/stream/stock/product/{productId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<List<StockResponse>> stockStream(@PathVariable final String productId) {
        return streamService.stockStream(productId);
    }

    @GetMapping(value = "/stream/stock/upc/{upc}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockResponse> skuStockStream(@PathVariable final Long upc) {
        return streamService.skuStockStream(upc);
    }
}