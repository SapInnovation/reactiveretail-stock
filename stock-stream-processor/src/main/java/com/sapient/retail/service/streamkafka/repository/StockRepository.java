package com.sapient.retail.service.streamkafka.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.sapient.retail.service.streamkafka.model.ProductStockInfo;

/**
 * ProductStockInfo Repository for Reactive MongoDB interactions
 * @author ragarora
 */
public interface StockRepository extends ReactiveMongoRepository<ProductStockInfo, Long> {
	
}