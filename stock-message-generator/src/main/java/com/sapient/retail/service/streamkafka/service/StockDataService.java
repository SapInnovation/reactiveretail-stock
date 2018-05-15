package com.sapient.retail.service.streamkafka.service;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.sapient.retail.service.streamkafka.model.Stock;
import com.sapient.retail.service.streamkafka.stream.StockDataStreams;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockDataService {
    private final StockDataStreams stockdatastreams;

    public StockDataService(StockDataStreams stockdatastreams) {
        this.stockdatastreams = stockdatastreams;
    }

    public void sendStockData(final Stock stockdata) {
        log.info("Sending stock data {}", stockdata);

        MessageChannel messageChannel = stockdatastreams.outboundStockData();
        messageChannel.send(MessageBuilder
                .withPayload(stockdata)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
