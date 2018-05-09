package com.sapient.retail.service.streamkafka.service;


import com.sapient.retail.service.streamkafka.model.StockData;
import com.sapient.retail.service.streamkafka.stream.StockStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component

public class StockListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockListener.class);
    @StreamListener(StockStreams.INPUT)


    public void handleStock(@Payload StockData stockdata)

    {
        LOGGER.info("Received StockInfo: {}", stockdata);
    }
}
