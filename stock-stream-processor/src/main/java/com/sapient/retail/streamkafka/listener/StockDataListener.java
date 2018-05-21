package com.sapient.retail.streamkafka.listener;

import com.sapient.retail.stock.common.model.impl.Stock;

public interface StockDataListener {

	/**
	 * This method listens to INPUT Kafka topic and persists data in Mongo repository 
	 * in a blocking mode and doesn't wait for any mono subscriptions.
	 * @param newStockDetails Stock
	 */
	void handleStockDataFromTopic(Stock newStockDetails);

}