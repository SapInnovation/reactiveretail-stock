package com.sapient.retail.service.streamkafka.service;


import com.sapient.retail.service.streamkafka.model.StockData;
import com.sapient.retail.service.streamkafka.stream.StockStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class StockService {
    private final StockStreams stockStreams;
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    public StockService(StockStreams stockStreams) {
        this.stockStreams = stockStreams;
    }

    public void sendStockData(final StockData stockData) {
        LOGGER.info("Sending stock data {}", stockData);

        MessageChannel messageChannel = stockStreams.outboundStockInfo();
        messageChannel.send(MessageBuilder
                .withPayload(stockData)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
