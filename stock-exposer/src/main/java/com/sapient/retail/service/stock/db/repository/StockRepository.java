package com.sapient.retail.service.stock.db.repository;

import com.sapient.retail.service.stock.db.beans.Stock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface StockRepository extends ReactiveMongoRepository<Stock, Integer> {
}
