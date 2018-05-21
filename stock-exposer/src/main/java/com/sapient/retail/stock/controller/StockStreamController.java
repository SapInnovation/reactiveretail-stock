package com.sapient.retail.stock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.model.StockResponse;

import reactor.core.publisher.Flux;

public interface StockStreamController {

	Flux<StockResponse> stockStream(String productId);

	Flux<StockResponse> skuStockStream(Long upc);

	Flux<RetailStock> stockStream();

}