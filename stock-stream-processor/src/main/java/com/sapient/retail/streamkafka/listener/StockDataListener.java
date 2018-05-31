package com.sapient.retail.streamkafka.listener;

import com.sapient.retail.stock.common.model.Stock;

/**
 * Interface to registering KAFKA stream listener. The method handleStockDataFromTopic
 * should be implemented by the classes for receiving messages. Use
 * {@link org.springframework.cloud.stream.annotation.StreamListener} annotations for reading
 * messages.
 *
 * @param <T> Stock implementation
 */
public interface StockDataListener<T extends Stock> {

    /**
     * Method for listening to KAFKA messages.
     *
     * @param newStockDetails received message
     */
    void handleStockDataFromTopic(T newStockDetails);

}
