package com.sapient.retail.service.streamkafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sapient.retail.model.ProductStock;
import com.sapient.retail.model.SkuStock;
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
	 * @param newProductStockDetails ProductStock
	 */
	@StreamListener(StockDataStreams.INPUT)
    public void handleStockDataFromTopic(@Payload ProductStock newProductStockDetails) {

		ProductStock existingProductStockDetailsUpdated = null;
		boolean stockExists = stockRepository.existsById(newProductStockDetails.getUpc()).block();

		if (stockExists) {
			ProductStock existingProductStockDetails = stockRepository.findById(newProductStockDetails.getUpc()).block();
			log.debug("New Prod Stock Details: {}", newProductStockDetails);

			for (SkuStock newSkuStock: newProductStockDetails.getStock()) {
				existingProductStockDetails.getStock().removeIf(
						skuStock -> 
							skuStock.getLocationId().equals(newSkuStock.getLocationId()));
			}
			log.debug("Existing Prod Stock Details after filter: {}", existingProductStockDetails);
			existingProductStockDetails.getStock().addAll(newProductStockDetails.getStock());
			log.debug("Existing Prod Stock Details after merge: {}", existingProductStockDetails);
			existingProductStockDetailsUpdated = stockRepository.save(existingProductStockDetails).block();
		} else {
			existingProductStockDetailsUpdated  = stockRepository.save(newProductStockDetails).block();
		}
    	log.info("Received stock message: {}", existingProductStockDetailsUpdated);
    }
}
