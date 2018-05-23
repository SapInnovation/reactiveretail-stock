package com.sapient.retail.streamkafka.listener;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.repository.StockRepository;
import com.sapient.retail.streamkafka.service.impl.StockDataService;
import com.sapient.retail.streamkafka.stream.StockDataStreams;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class StockDataListener {

    private final StockRepository stockRepository;
    private final StockDataService stockDataService;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Dependency injection based constructor.
     *
     * @param stockRepository  {@link StockRepository} instance
     * @param stockDataService {@link StockDataService} instance
     */
    public StockDataListener(final StockRepository stockRepository,
                             final StockDataService stockDataService) {
        this.stockRepository = stockRepository;
        this.stockDataService = stockDataService;
    }

    /**
     * This method listens to INPUT Kafka topic and persists data in Mongo repository
     * in a blocking mode and doesn't wait for any mono subscriptions.
     *
     * @param newStockDetails Stock
     */
    @StreamListener(StockDataStreams.INPUT)
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void handleStockDataFromTopic(final @Payload Stock newStockDetails) {

        Stock existingStockDetailsUpdated;
        boolean stockExists = stockRepository.existsById(newStockDetails.getUpc()).block();

        if (stockExists) {
            Stock existingStockDetails = stockRepository.findById(newStockDetails.getUpc()).block();
            log.debug("New Prod Stock Details: {}", newStockDetails);

            stockDataService.evaluateAvailableStock(newStockDetails, existingStockDetails);

            log.debug("Existing Prod Stock Details after merge: {}", existingStockDetails);
            existingStockDetailsUpdated = stockRepository.save(existingStockDetails).block();
        } else {
            existingStockDetailsUpdated = stockRepository.save(newStockDetails).block();
        }

        log.info("Received stock message: {}", existingStockDetailsUpdated);
    }
}
