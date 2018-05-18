package com.sapient.retail.stock.service.impl;

import com.sapient.retail.stock.common.repository.StockRepository;
import com.sapient.retail.stock.model.StockResponse;
import com.sapient.retail.stock.service.HelperService;
import com.sapient.retail.stock.service.StockService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final StockRepository repository;
    private final HelperService helper;

    public StockServiceImpl(final StockRepository repository,
                        final HelperService helper) {
        this.repository = repository;
        this.helper = helper;
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.StockService#productStock(java.lang.String)
	 */
    @Override
	public Mono<List<StockResponse>> productStock(final String productId) {
        LOGGER.debug("Fetching stock information for product: " + productId);
        return repository.findStocksByProductIdOrderByUpc(productId)
                .switchIfEmpty(helper.stockNotFound())
                .map(helper::buildFromStock)
                .collectList();
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.StockService#skuStock(java.lang.Long)
	 */
    @Override
	public Mono<StockResponse> skuStock(final Long upc) {
        LOGGER.debug("Fetching stock information for UPC: " + upc);
        return repository.findStocksByUpc(upc)
                .switchIfEmpty(helper.stockNotFound())
                .map(helper::buildFromStock);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.StockService#productStockForLocation(java.lang.String, java.lang.Long)
	 */
    @Override
	public Mono<List<StockResponse>> productStockForLocation(final String productId, final Long locationId) {
        LOGGER.debug("Fetching stock information for Product: " + productId + " & location: " + locationId);
        return repository.findStocksByProductIdOrderByUpc(productId)
                .switchIfEmpty(helper.stockNotFound())
                .map(stock -> helper.buildFromStock(stock, locationId))
                .collectList();
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.StockService#skuStockForLocation(java.lang.Long, java.lang.Long)
	 */
    @Override
	public Mono<StockResponse> skuStockForLocation(final Long upc, final Long locationId) {
        LOGGER.debug("Fetching stock information for UPC: " + upc + " & location: " + locationId);
        return repository.findStocksByUpc(upc)
                .switchIfEmpty(helper.stockNotFound())
                .map(stock -> helper.buildFromStock(stock, locationId));
    }
}
