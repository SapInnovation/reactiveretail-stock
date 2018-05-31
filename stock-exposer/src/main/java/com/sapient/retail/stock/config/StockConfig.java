package com.sapient.retail.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.SubscribableChannel;

@Configuration
public class StockConfig {
    @Bean
    public SubscribableChannel stockProductChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public SubscribableChannel stockSkuChannel() {
        return new PublishSubscribeChannel();
    }
}
