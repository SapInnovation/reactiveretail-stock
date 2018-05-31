package com.sapient.retail.streamkafka.listener.impl;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.repository.StockRepository;
import com.sapient.retail.streamkafka.listener.StockDataListener;
import com.sapient.retail.streamkafka.service.StockDataService;
import com.sapient.retail.streamkafka.stream.StockDataStreams;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Implementation specific to {@link RetailStock}. This will register KAFKA stream listener and handleStockDataFromTopic
 * method will receive messages sent on {@link StockDataStreams}.INPUT stream.
 */
@Component
public class RetailStockDataListener implements StockDataListener<RetailStock> {

    private final StockRepository<RetailStock> stockRepository;
    private final StockDataService<RetailStock> stockDataService;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * IOC based dependency constructor.
     *
     * @param stockRepository  DB repository instance
     * @param stockDataService data service instance
     */
    public RetailStockDataListener(final StockRepository<RetailStock> stockRepository,
                                   final StockDataService<RetailStock> stockDataService) {
        super();
        this.stockRepository = stockRepository;
        this.stockDataService = stockDataService;
    }

    /**
     * This method listens to {@link StockDataStreams}.INPUT stream. The received Stock is validated against
     * exising stock and then updated in DB.
     *
     * @param newStockDetails received stock update
     */
    @Override
    @StreamListener(StockDataStreams.INPUT)
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void handleStockDataFromTopic(final @Payload RetailStock newStockDetails) {
        RetailStock existingStockDetailsUpdated;
        Stock stock = stockRepository.findByUpc(newStockDetails.getUpc()).block();

        if (null != stock &&
                null != stock.getUpc()) {
            RetailStock existingStockDetails = stockRepository.findByUpc(newStockDetails.getUpc()).block();
            log.debug("New Prod Stock Details: {}", newStockDetails);

            stockDataService.compareAndMergeStock(newStockDetails, existingStockDetails);

            log.debug("Existing Prod Stock Details after merge: {}", existingStockDetails);
            existingStockDetailsUpdated = stockRepository.save(existingStockDetails).block();
        } else {
            stockDataService.validateStock(newStockDetails);
            existingStockDetailsUpdated = stockRepository.save(newStockDetails).block();
        }
        log.info("Received stock message: {}", existingStockDetailsUpdated);
    }
}
