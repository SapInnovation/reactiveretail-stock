package com.sapient.retail.service.streamkafka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sapient.retail.service.streamkafka.service.StockDataService;
import com.sapient.retail.service.streamkafka.stream.StockDataStreams;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.repository.StockRepository;

@Component
public class StockDataListener {
	
	private final StockRepository stockRepository;
	private final StockDataService stockDataService;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @param stockRepository
	 * @param stockDataService
	 */
	public StockDataListener(StockRepository stockRepository, StockDataService stockDataService) {
		super();
		this.stockRepository = stockRepository;
		this.stockDataService = stockDataService;
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

			stockDataService.evaluateAvailableStock(newStockDetails, existingStockDetails);

			log.debug("Existing Prod Stock Details after merge: {}", existingStockDetails);
			existingStockDetailsUpdated = stockRepository.save(existingStockDetails).block();
		} else {
			existingStockDetailsUpdated  = stockRepository.save(newStockDetails).block();
		}
    	log.info("Received stock message: {}", existingStockDetailsUpdated);
    }

}
