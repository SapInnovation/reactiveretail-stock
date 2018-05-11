package com.sapient.retail.service.streamkafka.config;

import com.sapient.retail.service.streamkafka.stream.StockDataStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(StockDataStreams.class)
public class StreamsConfig {
}
