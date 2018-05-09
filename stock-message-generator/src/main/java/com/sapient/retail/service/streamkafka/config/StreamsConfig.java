package com.sapient.retail.service.streamkafka.config;

import com.sapient.retail.service.streamkafka.stream.StockStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(StockStreams.class)
public class StreamsConfig {
}
