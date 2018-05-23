package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.model.impl.RetailStockInfo;
import com.sapient.retail.stock.exception.StockNotFoundException;
import com.sapient.retail.stock.model.StockResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

/**
 * Helper Service for {@link Stock} & {@link StockResponse} object creation tasks.
 */
@Service
public class HelperService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${custom.default-location}")
    private long defaultLocationId;

    /**
     * Return a Mono in case Stock is not available for the query.
     *
     * @return Mono object
     */
    public Mono<RetailStock> stockNotFound() {
        return Mono.just(GenericBuilder.of(RetailStock::new)
                .with(Stock::setUpc, -1000000L)
                .build());
    }

    /**
     * Method to build {@link StockResponse} from DB response for default store.
     * This method will throw {@link StockNotFoundException} in case stock is
     * not available in db.
     *
     * @param stock the DB stock response object
     * @return the {@link StockResponse} object
     */
    public StockResponse buildFromStock(final Stock stock) {
        return buildFromStock(stock, defaultLocationId);
    }

    /**
     * Method to build {@link StockResponse} from DB response for given store.
     * This method will throw {@link StockNotFoundException} in case stock is
     * not available in db.
     *
     * @param stock the DB stock response object
     * @return the {@link StockResponse} object
     */
    public StockResponse buildFromStock(final Stock stock, final Long locationId) {
        if (-1000000L == stock.getUpc()) {
            throw new StockNotFoundException();
        }

        StockInfo stockInfo = new StockInfo();
        if ((stock instanceof RetailStock) && ((RetailStock)stock).getStock().containsKey(locationId)) {
            LOGGER.debug(new StringBuilder("Stock Response getting built from Stock for UPC: ")
                    .append(stock.getUpc())
                    .append(" & Product: ").append(stock.getProductId())
                    .append(" & location: ").append(locationId).toString());
            stockInfo = ((RetailStock)stock).getStock().get(locationId);
        }
        return GenericBuilder.of(StockResponse::new)
                .with(StockResponse::setUpc, stock.getUpc())
                .with(StockResponse::setProductId, stock.getProductId())
                .with(StockResponse::setLocationId, locationId)
                .with(StockResponse::setLocationName, stockInfo.getLocationName())
                .with(StockResponse::setAvailableStock, stockInfo.getAvailableStock())
                .build();
    }
}
