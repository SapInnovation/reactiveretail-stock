package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.Stock;
import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private SubscribableChannel stockProductChannel;

    public MessageService(final SubscribableChannel stockProductChannel) {
        this.stockProductChannel = stockProductChannel;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Publisher(channel = "stockProductChannel")
    Stock publishMessage(final Stock stockUpdate) {
        return stockUpdate;
    }

    SubscribableChannel getProductStockChannel() {
        return stockProductChannel;
    }


}
