package com.sapient.retail.stock.controller;

import reactor.core.publisher.Flux;

import com.sapient.retail.stock.common.model.impl.Stock;
import com.sapient.retail.stock.model.StockResponse;

public interface StockStreamController {

	Flux<StockResponse> stockStream(String productId);

	Flux<StockResponse> skuStockStream(Long upc);

	Flux<Stock> stockStream();

}