package com.sapient.retail.stock.common.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.sapient.retail.stock.common.model.impl.RetailStock;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockRepository extends ReactiveMongoRepository<RetailStock, Long> {
    Flux<RetailStock> findStocksByProductIdOrderByUpc(final String productId);
    
    Mono<RetailStock> findStocksByUpc(final Long upc);
    
    @Query(value = "{ 'upc' : ?0, 'stock.locationId' : ?1 }")
    Mono<RetailStock> findStocksByUpcAndLocationId(final Long upc, final Long locationId);
    
    @Query(value = "{ 'productId' : ?0, 'stock.locationId' : ?1 }")
    Flux<RetailStock> findStocksByProductAndLocationId(final String productId, final Long locationId);
}
