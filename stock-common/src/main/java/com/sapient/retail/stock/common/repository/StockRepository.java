package com.sapient.retail.stock.common.repository;

import com.sapient.retail.stock.common.model.Stock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface StockRepository extends ReactiveMongoRepository<Stock, Long> {
    Flux<Stock> findStocksByProductIdOrderByUpc(final String productId);
}
