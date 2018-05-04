package com.sapient.retail.service.stock.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.SubscribableChannel;

@Configuration
public class StockConfiguration {
    @Bean
    SubscribableChannel stockChannel() {
        return MessageChannels.publishSubscribe().get();
    }
}
