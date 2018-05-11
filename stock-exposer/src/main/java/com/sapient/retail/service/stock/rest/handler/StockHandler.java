package com.sapient.retail.service.stock.rest.handler;

import com.sapient.retail.service.stock.db.beans.Stock;
import com.sapient.retail.service.stock.db.repository.StockRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
public class StockHandler {

    private final StockRepository stockRepository;

    public StockHandler(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Mono<ServerResponse> allStock(final ServerRequest serverRequest) {
        return ServerResponse.ok().body(stockRepository.findAll(), Stock.class);
    }
}
