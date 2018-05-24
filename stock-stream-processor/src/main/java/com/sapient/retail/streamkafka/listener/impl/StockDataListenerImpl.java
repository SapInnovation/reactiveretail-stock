package com.sapient.retail.streamkafka.listener.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.repository.StockRepository;
import com.sapient.retail.streamkafka.listener.StockDataListener;
import com.sapient.retail.streamkafka.service.StockDataService;
import com.sapient.retail.streamkafka.stream.StockDataStreams;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Component
public class StockDataListenerImpl implements StockDataListener<RetailStock> {
	
	private final StockRepository<RetailStock> stockRepository;
	private final StockDataService<RetailStock> stockDataService;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @param stockRepository
	 * @param stockDataService
	 */
	public StockDataListenerImpl(final StockRepository<RetailStock> stockRepository,
								 final StockDataService<RetailStock> stockDataService) {
		super();
		this.stockRepository = stockRepository;
		this.stockDataService = stockDataService;
	}

	/* (non-Javadoc)
	 * @see com.sapient.retail.streamkafka.listener.StockDataListener#handleStockDataFromTopic(com.sapient.retail.stock.common.model.Stock)
	 * This method listens to INPUT Kafka topic and persists data in Mongo repository 
	 * in a blocking mode and doesn't wait for any mono subscriptions.
	 * @param newStockDetails Stock
	 */
	@Override
	@StreamListener(StockDataStreams.INPUT)
	@SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void handleStockDataFromTopic(@Payload RetailStock newStockDetails) {

		RetailStock existingStockDetailsUpdated = null;
		boolean stockExists = stockRepository.existsById(newStockDetails.getUpc()).block();

		if (stockExists) {
			RetailStock existingStockDetails = stockRepository.findById(newStockDetails.getUpc()).block();
			log.debug("New Prod Stock Details: {}", newStockDetails);

			stockDataService.evaluateAvailableStock(newStockDetails, existingStockDetails);

			log.debug("Existing Prod Stock Details after merge: {}", existingStockDetails);
			existingStockDetailsUpdated = stockRepository.save(existingStockDetails).block();
		} else {
			stockDataService.evaluateNewStock(newStockDetails);
			existingStockDetailsUpdated  = stockRepository.save(newStockDetails).block();
		}
    	log.info("Received stock message: {}", existingStockDetailsUpdated);
    }

}
