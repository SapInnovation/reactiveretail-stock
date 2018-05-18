package com.sapient.retail.stock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sapient.retail.stock.model.StockResponse;

import reactor.core.publisher.Mono;

public interface StockController {

	Mono<List<StockResponse>> productStock(String productId);

	Mono<StockResponse> skuStock(Long upc);

	Mono<StockResponse> skuStockForLocation(Long upc, Long locationId);

	Mono<List<StockResponse>> stockForLocation(String productId, Long locationId);

}