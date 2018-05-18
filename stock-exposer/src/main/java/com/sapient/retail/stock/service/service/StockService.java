package com.sapient.retail.stock.service.service;

import com.sapient.retail.stock.common.repository.StockRepository;
import com.sapient.retail.stock.service.model.StockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StockService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final StockRepository repository;
    private final HelperService helper;

    public StockService(final StockRepository repository,
                        final HelperService helper) {
        this.repository = repository;
        this.helper = helper;
    }

    /**
     * Method to lookup Database for Stock information for requested Product
     *
     * @param productId the product Id
     * @return Mono object containing a {@link List} of Stock {@link StockResponse}
     */
    public Mono<List<StockResponse>> productStock(final String productId) {
        LOGGER.debug("Fetching stock information for product: " + productId);
        return repository.findStocksByProductIdOrderByUpc(productId)
                .switchIfEmpty(helper.stockNotFound())
                .map(helper::buildFromStock)
                .collectList();
    }

    /**
     * Method to lookup Database for Stock information for requested UPC
     *
     * @param upc the SKU upc
     * @return Mono object containing Stock {@link StockResponse}
     */
    public Mono<StockResponse> skuStock(final Long upc) {
        LOGGER.debug("Fetching stock information for UPC: " + upc);
        return repository.findStocksByUpc(upc)
                .switchIfEmpty(helper.stockNotFound())
                .map(helper::buildFromStock);
    }

    /**
     * Method to lookup Database for Stock details for provided ProductId and LocationId
     *
     * @param productId  the product Id
     * @param locationId the store location Id
     * @return Mono object containing a {@link List} of Stock {@link StockResponse}
     */
    public Mono<List<StockResponse>> productStockForLocation(final String productId, final Long locationId) {
        LOGGER.debug("Fetching stock information for Product: " + productId + " & location: " + locationId);
        return repository.findStocksByProductIdOrderByUpc(productId)
                .switchIfEmpty(helper.stockNotFound())
                .map(stock -> helper.buildFromStock(stock, locationId))
                .collectList();
    }

    /**
     * Method to lookup Database for Stock details for provided UPC and LocationId
     *
     * @param upc        the SKU upc
     * @param locationId the store location Id
     * @return Mono<StockResponse> with only requested UPC and locationId, if present
     */
    public Mono<StockResponse> skuStockForLocation(final Long upc, final Long locationId) {
        LOGGER.debug("Fetching stock information for UPC: " + upc + " & location: " + locationId);
        return repository.findStocksByUpc(upc)
                .switchIfEmpty(helper.stockNotFound())
                .map(stock -> helper.buildFromStock(stock, locationId));
    }
}
