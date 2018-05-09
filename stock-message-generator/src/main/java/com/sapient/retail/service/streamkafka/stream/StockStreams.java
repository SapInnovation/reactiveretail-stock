package com.sapient.retail.service.streamkafka.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StockStreams {
    String INPUT = "stockinfo-in";
    String OUTPUT = "stockinfo-out";

    @Input(INPUT)
    SubscribableChannel inboundStockInfo();

    @Output(OUTPUT)
    MessageChannel outboundStockInfo();
}
