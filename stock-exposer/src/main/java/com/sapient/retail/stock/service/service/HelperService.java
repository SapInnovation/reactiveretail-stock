package com.sapient.retail.stock.service.service;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;
import com.sapient.retail.stock.service.exception.StockNotFoundException;
import com.sapient.retail.stock.service.model.StockResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Helper Service for {@link Stock} & {@link StockResponse} object creation tasks.
 */
@Service
public class HelperService {

    @Value(value = "${custom.default-location}")
    private long defaultLocationId;

    /**
     * Return a Mono in case Stock is not available for the query.
     *
     * @return Mono object
     */
    Mono<Stock> stockNotFound() {
        return Mono.just(GenericBuilder.of(Stock::new)
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
    StockResponse buildFromStock(final Stock stock) {
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
    StockResponse buildFromStock(final Stock stock, final Long locationId) {
        if (null == stock
                || -1000000L == stock.getUpc())
            throw new StockNotFoundException();

        StockInfo stockInfo = new StockInfo();
        if (stock.getStock().containsKey(locationId))
            stockInfo = stock.getStock().get(locationId);
        return GenericBuilder.of(StockResponse::new)
                .with(StockResponse::setUpc, stock.getUpc())
                .with(StockResponse::setProductId, stock.getProductId())
                .with(StockResponse::setLocationId, locationId)
                .with(StockResponse::setLocationName, stockInfo.getLocationName())
                .with(StockResponse::setAvailableStock, stockInfo.getAvailableStock())
                .build();
    }
}
