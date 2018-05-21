package com.sapient.retail.stock.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.impl.Stock;
import com.sapient.retail.stock.common.model.impl.StockInfo;
import com.sapient.retail.stock.exception.StockNotFoundException;
import com.sapient.retail.stock.model.StockResponse;
import com.sapient.retail.stock.service.HelperService;

import reactor.core.publisher.Mono;

/**
 * Helper Service for {@link Stock} & {@link StockResponse} object creation tasks.
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
	public Mono<Stock> stockNotFound() {
        return Mono.just(GenericBuilder.of(Stock::new)
                .with(Stock::setUpc, -1000000L)
                .build());
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.HelperService#buildFromStock(com.sapient.retail.stock.common.model.Stock)
	 */
    @Override
	public StockResponse buildFromStock(final Stock stock) {
        return buildFromStock(stock, defaultLocationId);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.HelperService#buildFromStock(com.sapient.retail.stock.common.model.Stock, java.lang.Long)
	 */
    @Override
	public StockResponse buildFromStock(final Stock stock, final Long locationId) {
        if (null == stock || -1000000L == stock.getUpc()) {
        	LOGGER.error("Error while building stock details for UPC: " + stock.getUpc() + " & location: " + locationId);
        	throw new StockNotFoundException();
        }

        StockInfo stockInfo = new StockInfo();
        if (stock.getStock().containsKey(locationId)) {
        	LOGGER.debug(new StringBuilder("Stock Response getting built from Stock for UPC: ")
        			.append(stock.getUpc())
        			.append(" & Product: ").append(stock.getProductId())
        			.append(" & location: ").append(locationId).toString());
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
