package com.sapient.retail.streamkafka.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.sapient.retail.streamkafka.stream.StockDataStreams;

@EnableBinding(StockDataStreams.class)
public class StreamsConfig {
}
