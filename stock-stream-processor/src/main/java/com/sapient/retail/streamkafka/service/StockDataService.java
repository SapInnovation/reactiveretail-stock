package com.sapient.retail.streamkafka.service;

import com.sapient.retail.stock.common.model.impl.Stock;

public interface StockDataService {

	void sendStockDataToKafkaTopic(Stock productStock);

	/**
	 * Method to evaluate supply, demand, available stock and persist them along with the updates to 
	 * all objects of Stock as per request including information source.
	 * @param newStockDetails
	 * @param existingStockDetails
	 */
	void evaluateAvailableStock(Stock newStockDetails, Stock existingStockDetails);

}