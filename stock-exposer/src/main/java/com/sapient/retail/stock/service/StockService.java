package com.sapient.retail.stock.service;

import java.util.List;

import com.sapient.retail.stock.model.StockResponse;

import reactor.core.publisher.Mono;

public interface StockService {

	/**
	 * Method to lookup Database for Stock information for requested Product
	 *
	 * @param productId the product Id
	 * @return Mono object containing a {@link List} of Stock {@link StockResponse}
	 */
	Mono<List<StockResponse>> productStock(String productId);

	/**
	 * Method to lookup Database for Stock information for requested UPC
	 *
	 * @param upc the SKU upc
	 * @return Mono object containing Stock {@link StockResponse}
	 */
	Mono<StockResponse> skuStock(Long upc);

	/**
	 * Method to lookup Database for Stock details for provided ProductId and LocationId
	 *
	 * @param productId  the product Id
	 * @param locationId the store location Id
	 * @return Mono object containing a {@link List} of Stock {@link StockResponse}
	 */
	Mono<List<StockResponse>> productStockForLocation(String productId, Long locationId);

	/**
	 * Method to lookup Database for Stock details for provided UPC and LocationId
	 *
	 * @param upc        the SKU upc
	 * @param locationId the store location Id
	 * @return Mono<StockResponse> with only requested UPC and locationId, if present
	 */
	Mono<StockResponse> skuStockForLocation(Long upc, Long locationId);

}