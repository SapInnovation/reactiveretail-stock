package com.sapient.retail.service.streamkafka.service;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sapient.retail.service.streamkafka.model.ProductStockInfo;
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

	@StreamListener(StockDataStreams.INPUT)
    public void handleGreetings(@Payload ProductStockInfo stockInfo) {
    	ProductStockInfo stockUpdatedInfo = stockRepository.save(stockInfo).block();
    	log.info("Received stock message: {}", stockUpdatedInfo);
    }
}
