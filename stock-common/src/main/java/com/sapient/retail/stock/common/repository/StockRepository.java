package com.sapient.retail.stock.common.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.sapient.retail.stock.common.model.Stock;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockRepository extends ReactiveMongoRepository<Stock, Long> {
    Flux<Stock> findStocksByProductIdOrderByUpc(final String productId);
    
    Mono<Stock> findStocksByUpc(final Long upc);
    
    @Query(value = "{ 'upc' : ?0, 'stock.locationId' : ?1 }")
    Mono<Stock> findStocksByUpcAndLocationId(final Long upc, final Long locationId);
    
    @Query(value = "{ 'productId' : ?0, 'stock.locationId' : ?1 }")
    Flux<Stock> findStocksByProductAndLocationId(final String productId, final Long locationId);
}
