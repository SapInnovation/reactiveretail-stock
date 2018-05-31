package com.sapient.retail.streamkafka.service;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.streamkafka.stream.StockDataStreams;
import org.springframework.integration.annotation.Publisher;

public abstract class StockDataService<T extends Stock> {

	@Publisher(StockDataStreams.OUTPUT)
    public Stock sendStockDataToKafkaTopic(final Stock stock) {
	    return stock;
    }

	/**
	 * Method to evaluate supply, demand, available stock and persist them along with the updates to 
	 * all objects of Stock as per request including information source.
	 * @param newStockDetails Updated stock details
	 * @param existingStockDetails existing stock
	 */
	public void compareAndMergeStock(T newStockDetails, T existingStockDetails) {

	}
	
	public void validateStock(T newStockDetails) {

    }
}