package com.sapient.retail.streamkafka.listener;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.streamkafka.stream.StockDataStreams;

public interface StockDataListener {

	/**
	 * This method listens to INPUT Kafka topic and persists data in Mongo repository 
	 * in a blocking mode and doesn't wait for any mono subscriptions.
	 * @param newStockDetails Stock
	 */
	void handleStockDataFromTopic(Stock newStockDetails);

}