package com.sapient.retail.service.streamkafka.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

<<<<<<< HEAD
import com.sapient.retail.service.streamkafka.model.ProductStockInfo;

/**
 * ProductStockInfo Repository for Reactive MongoDB interactions
 * @author ragarora
 */
public interface StockRepository extends ReactiveMongoRepository<ProductStockInfo, Integer> {
=======
import com.sapient.retail.service.streamkafka.model.Stock;

/**
 * Stock Repository for Reactive MongoDB interactions
 * @author ragarora
 */
public interface StockRepository extends ReactiveMongoRepository<Stock, Integer> {
>>>>>>> db927a509a0b14c2f2e978de3e0af25788f04e6e
	
}
