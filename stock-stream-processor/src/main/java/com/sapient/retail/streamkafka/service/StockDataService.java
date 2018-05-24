package com.sapient.retail.streamkafka.service;

import com.sapient.retail.stock.common.model.Stock;

public interface StockDataService<T extends Stock> {

	void sendStockDataToKafkaTopic(T productStock);

	/**
	 * Method to evaluate supply, demand, available stock and persist them along with the updates to 
	 * all objects of Stock as per request including information source.
	 * @param newStockDetails
	 * @param existingStockDetails
	 */
	void evaluateAvailableStock(T newStockDetails, T existingStockDetails);

}