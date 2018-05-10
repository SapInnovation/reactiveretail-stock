package com.sapient.retail.service.stock.rest.controller;

import com.sapient.retail.service.stock.db.beans.Stock;
import com.sapient.retail.service.stock.db.repository.StockRepository;
import com.sapient.retail.service.stock.service.StreamService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class StreamController {

    private final StockRepository stockRepository;
    private StreamService streamService;

    public StreamController(final StockRepository stockRepository,
                            final StreamService streamService) {
        this.stockRepository = stockRepository;
        this.streamService = streamService;
    }

    @PostMapping(value = "/stock/createStock")
    public Mono<Stock> createStock(@Valid @RequestBody Stock stock) {
        return stockRepository.save(stock);
    }

    @GetMapping(value = "/stream/{skuId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Stock> stockStream(@PathVariable final int skuId) {
        return streamService.registerStream(skuId);
    }
}
