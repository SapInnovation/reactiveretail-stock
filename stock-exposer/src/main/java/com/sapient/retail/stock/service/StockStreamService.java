package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.model.StockResponse;

import reactor.core.publisher.Flux;

public interface StockStreamService {

	/**
	 * Method to open stream for requested product with any SKU/location/stock getting updated in DB collection.
	 *
	 * @param productId the product ID
	 * @return Flux stream of {@link StockResponse} for all updates to requested product
	 */
	Flux<StockResponse> stockStream(String productId);

	/**
	 * Method to open stream for requested UPC with any location/stock getting updated in DB collection.
	 *
	 * @param upc the upc for the SKU
	 * @return Flux stream of {@link StockResponse} for all updates to requested UPC
	 */
	Flux<StockResponse> skuStockStream(Long upc);

	/**
	 * Method to open a stream and respond back with stock information for all products
	 *
	 * @return Flux<Stock> for all available products in DB collection
	 */
	Flux<Stock> allStockStream();

}