package com.sapient.retail.service.streamkafka.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.sapient.retail.service.streamkafka.stream.StockDataStreams;

@EnableBinding(StockDataStreams.class)
public class StreamsConfig {
}
