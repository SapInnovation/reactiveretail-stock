package com.sapient.retail.service.streamkafka.service;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sapient.retail.service.streamkafka.model.ProductStockInfo;
import com.sapient.retail.service.streamkafka.model.StockInfo;
import com.sapient.retail.service.streamkafka.repository.StockRepository;
import com.sapient.retail.service.streamkafka.stream.StockDataStreams;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StockDataListener {
	
	private final StockRepository stockRepository;
	
	public StockDataListener(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

	/**
	 * This method listens to INPUT Kafka topic and persists data in Mongo repository 
	 * in a block mode and doesn't wait for any mono subscription.
	 * @param stockInfo ProductStockInfo
	 */
	@StreamListener(StockDataStreams.INPUT)
    public void handleGreetings(@Payload ProductStockInfo newProductStockDetails) {

		ProductStockInfo existingProductStockDetailsUpdated = null;
		boolean stockExists = stockRepository.existsById(newProductStockDetails.getUpc()).block();

		if (stockExists) {
			ProductStockInfo existingProductStockDetails = stockRepository.findById(newProductStockDetails.getUpc()).block();
			log.debug("New Prod Stock Details: {}", newProductStockDetails);

			for (StockInfo newStockInfo: newProductStockDetails.getStock()) {
				existingProductStockDetails.getStock().removeIf(
						stockInfo -> 
							stockInfo.getLocationId().equals(newStockInfo.getLocationId()));
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
