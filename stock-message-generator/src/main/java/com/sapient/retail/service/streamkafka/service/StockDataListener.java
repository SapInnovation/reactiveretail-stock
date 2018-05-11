package com.sapient.retail.service.streamkafka.service;

import com.sapient.retail.service.streamkafka.model.StockData;
import com.sapient.retail.service.streamkafka.stream.StockDataStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockDataListener {
    @StreamListener(StockDataStreams.INPUT)
    public void handleGreetings(@Payload StockData stockdata) {
        log.info("Received stock message: {}", stockdata);
    }
}
