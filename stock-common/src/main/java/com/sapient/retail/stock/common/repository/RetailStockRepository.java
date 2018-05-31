package com.sapient.retail.stock.common.repository;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RetailStockRepository extends StockRepository<RetailStock>,
        ReactiveMongoRepository<RetailStock, Long> {
}
