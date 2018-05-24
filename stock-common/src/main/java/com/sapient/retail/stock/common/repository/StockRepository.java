package com.sapient.retail.stock.common.repository;

import com.sapient.retail.stock.common.model.Stock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockRepository<T extends Stock> extends ReactiveMongoRepository<T, Long> {
    Flux<T> findStocksByProductIdOrderByUpc(final String productId);
    
    Mono<T> findStocksByUpc(final Long upc);
}
