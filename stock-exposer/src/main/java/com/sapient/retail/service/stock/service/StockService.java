package com.sapient.retail.service.stock.service;

import com.sapient.retail.service.stock.db.beans.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Publisher(channel = "stockChannel")
    public Stock publishStock(final Stock stock) {
        LOGGER.info("Publishing Stock Update Event");
        return stock;
    }
}
