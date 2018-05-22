package com.sapient.retail.stock.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.model.impl.RetailStockInfo;
import com.sapient.retail.stock.exception.StockNotFoundException;
import com.sapient.retail.stock.model.StockResponse;
import com.sapient.retail.stock.service.HelperService;

import reactor.core.publisher.Mono;

/**
 * Helper Service for {@link RetailStock} & {@link StockResponse} object creation tasks.
 */
@Service
public class HelperServiceImpl implements HelperService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
    @Value(value = "${custom.default-location}")
    private long defaultLocationId;

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.HelperService#stockNotFound()
	 */
    @Override
	public Mono<RetailStock> stockNotFound() {
        return Mono.just(GenericBuilder.of(RetailStock::new)
                .with(RetailStock::setUpc, -1000000L)
                .build());
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.HelperService#buildFromStock(com.sapient.retail.stock.common.model.Stock)
	 */
    @Override
	public StockResponse buildFromStock(final RetailStock retailStock) {
        return buildFromStock(retailStock, defaultLocationId);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.HelperService#buildFromStock(com.sapient.retail.stock.common.model.Stock, java.lang.Long)
	 */
    @Override
	public StockResponse buildFromStock(final RetailStock retailStock, final Long locationId) {
        if (null == retailStock || -1000000L == retailStock.getUpc()) {
        	LOGGER.error("Error while building stock details for UPC: " + retailStock.getUpc() + " & location: " + locationId);
        	throw new StockNotFoundException();
        }

        RetailStockInfo retailStockInfo = new RetailStockInfo();
        if (retailStock.getStock().containsKey(locationId)) {
        	LOGGER.debug(new StringBuilder("Stock Response getting built from Stock for UPC: ")
        			.append(retailStock.getUpc())
        			.append(" & Product: ").append(retailStock.getProductId())
        			.append(" & location: ").append(locationId).toString());
        	retailStockInfo = retailStock.getStock().get(locationId);
        }
        return GenericBuilder.of(StockResponse::new)
                .with(StockResponse::setUpc, retailStock.getUpc())
                .with(StockResponse::setProductId, retailStock.getProductId())
                .with(StockResponse::setLocationId, locationId)
                .with(StockResponse::setLocationName, retailStockInfo.getLocationName())
                .with(StockResponse::setAvailableStock, retailStockInfo.getAvailableStock())
                .build();
    }
}
