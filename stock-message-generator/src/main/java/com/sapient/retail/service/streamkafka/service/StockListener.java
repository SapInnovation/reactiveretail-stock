package com.sapient.retail.service.streamkafka.service;

import com.sapient.retail.service.streamkafka.model.Greetings;
import com.sapient.retail.service.streamkafka.model.StockData;
import com.sapient.retail.service.streamkafka.stream.StockStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockListener {
    @StreamListener(StockStreams.INPUT)
    public void handleStock(@Payload StockData stockdata) {
        log.info("Received StockInfo: {}", stockdata);
    }
}
