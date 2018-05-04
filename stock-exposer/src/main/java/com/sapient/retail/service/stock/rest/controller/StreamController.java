package com.sapient.retail.service.stock.rest.controller;

import com.sapient.retail.service.stock.db.beans.Stock;
import com.sapient.retail.service.stock.db.repository.StockRepository;
import com.sapient.retail.service.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class StreamController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final StockRepository stockRepository;
    private SubscribableChannel stockChannel;
    private StockService stockService;

    public StreamController(final StockRepository stockRepository,
                        final SubscribableChannel stockChannel,
                        final StockService stockService) {
        this.stockRepository = stockRepository;
        this.stockChannel = stockChannel;
        this.stockService = stockService;
    }

    @PostMapping(value = "/stock/createStock")
    public Mono<Stock> createStock(@Valid @RequestBody Stock stock) {
        stockService.publishStock(stock);
        return stockRepository.save(stock);
    }

    @GetMapping(value = "/stream/{skuId}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Stock> stockStream(@PathVariable final int skuId) {
        return Flux.create(stream -> {
            MessageHandler handler = msg -> {
                final Stock stock = Stock.class.cast(msg.getPayload());
                LOGGER.info("Request skuId:" + skuId + ", Current Event skuId:" + stock.getSkuId());
                if (skuId == stock.getSkuId())
                    stream.next(stock);
            };
            stream.onCancel(() -> stockChannel.unsubscribe(handler));
            stockChannel.subscribe(handler);
        });
    }
}
