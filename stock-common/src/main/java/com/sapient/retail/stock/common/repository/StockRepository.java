package com.sapient.retail.stock.common.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.sapient.retail.stock.common.model.Stock;

import reactor.core.publisher.Flux;

public interface StockRepository extends ReactiveMongoRepository<Stock, Long> {
    Flux<Stock> findStocksByProductIdOrderByUpc(final String productId);
    
    Flux<Stock> findStocksByUpc(final Long upc);
    
    @Query(value = "{ 'upc' : ?0, 'stock.locationId' : ?1 }")
    Flux<Stock> findStocksByUpcAndLocationId(final Long upc, final Long locationId);
}
