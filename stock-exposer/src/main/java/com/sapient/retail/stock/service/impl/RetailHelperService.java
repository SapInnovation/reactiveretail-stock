package com.sapient.retail.stock.service.impl;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.model.impl.StockResponse;
import com.sapient.retail.stock.exception.StockNotFoundException;
import com.sapient.retail.stock.service.HelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * HelperService for {@link Stock} & {@link StockResponse} object creation tasks.
 */
@Service
public class RetailHelperService implements HelperService<RetailStock, StockResponse> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${custom.default-location}")
    private long defaultLocationId;

    /**
     * Return a Mono in case Stock is not available for the query.
     *
     * @return Mono object
     */
    @Override
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
    @Override
    public StockResponse buildFromStock(final RetailStock stock) {
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
    @Override
    public StockResponse buildFromStock(final RetailStock stock, final Long locationId) {
        if (-1000000L == stock.getUpc()) {
            throw new StockNotFoundException();
        }

        StockInfo stockInfo = new StockInfo();
        if (stock.getStock().containsKey(locationId)) {
            LOGGER.debug("Stock Response getting built from Stock for UPC: ",
                    stock.getUpc(), " & Product: ", stock.getProductId(), " & location: ", locationId);
            stockInfo = stock.getStock().get(locationId);
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
