package com.sapient.retail.service.streamkafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.sapient.retail.model.ProductStock;
import com.sapient.retail.service.streamkafka.stream.StockDataStreams;

@Service
public class StockDataService {
    private final StockDataStreams stockdatastreams;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public StockDataService(StockDataStreams stockdatastreams) {
        this.stockdatastreams = stockdatastreams;
    }

    public void sendStockData(final ProductStock productStock) {
    	logger.info("Sending stock data {}", productStock);

        MessageChannel messageChannel = stockdatastreams.outboundStockData();
        messageChannel.send(MessageBuilder
                .withPayload(productStock)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
