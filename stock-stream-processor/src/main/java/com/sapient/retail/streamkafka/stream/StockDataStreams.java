package com.sapient.retail.streamkafka.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StockDataStreams {
    String INPUT = "stockdata-in";
    String OUTPUT = "stockdata-out";

    @Input(INPUT)
    SubscribableChannel inboundStockData();

    @Output(OUTPUT)
    MessageChannel outboundStockData();
}
