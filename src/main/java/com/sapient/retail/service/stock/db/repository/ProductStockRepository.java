package com.sapient.retail.service.stock.db.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.sapient.retail.service.stock.db.beans.ProductStock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author ragarora
 */
@Repository
public interface ProductStockRepository extends ReactiveMongoRepository<ProductStock, String> {
}
