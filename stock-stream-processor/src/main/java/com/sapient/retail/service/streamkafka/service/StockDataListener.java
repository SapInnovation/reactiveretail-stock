package com.sapient.retail.service.streamkafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;
import com.sapient.retail.service.streamkafka.repository.StockRepository;
import com.sapient.retail.service.streamkafka.stream.StockDataStreams;

@Component
public class StockDataListener {
	
	private final StockRepository stockRepository;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public StockDataListener(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

	/**
	 * This method listens to INPUT Kafka topic and persists data in Mongo repository 
	 * in a blocking mode and doesn't wait for any mono subscriptions.
	 * @param newStockDetails Stock
	 */
	@StreamListener(StockDataStreams.INPUT)
    public void handleStockDataFromTopic(@Payload Stock newStockDetails) {

		Stock existingStockDetailsUpdated = null;
		boolean stockExists = stockRepository.existsById(newStockDetails.getUpc()).block();

		if (stockExists) {
			Stock existingStockDetails = stockRepository.findById(newStockDetails.getUpc()).block();
			log.debug("New Prod Stock Details: {}", newStockDetails);

			for (StockInfo newStockInfo: newStockDetails.getStock()) {
				existingStockDetails.getStock().removeIf(
						skuStock -> 
							skuStock.getLocationId().equals(newStockInfo.getLocationId()));
			}
			log.debug("Existing Prod Stock Details after filter: {}", existingStockDetails);
			existingStockDetails.getStock().addAll(newStockDetails.getStock());
			log.debug("Existing Prod Stock Details after merge: {}", existingStockDetails);
			existingStockDetailsUpdated = stockRepository.save(existingStockDetails).block();
		} else {
			existingStockDetailsUpdated  = stockRepository.save(newStockDetails).block();
		}
    	log.info("Received stock message: {}", existingStockDetailsUpdated);
    }
}
