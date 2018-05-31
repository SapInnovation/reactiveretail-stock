package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.Response;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.impl.StockResponse;
import com.sapient.retail.stock.common.repository.StockRepository;
import com.sapient.retail.stock.service.impl.RetailHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StockService<T extends Stock, R extends Response> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final StockRepository<T> repository;
    private final HelperService<T, R> helperService;

    /**
     * Dependency injection based constructor.
     *
     * @param repository {@link StockRepository} instance
     * @param helperService     {@link RetailHelperService} for common tasks
     */
    public StockService(final StockRepository<T> repository,
                        final HelperService<T, R> helperService) {
        this.repository = repository;
        this.helperService = helperService;
    }

    /**
     * Method to lookup Database for Stock information for requested Product
     *
     * @param productId the product Id
     * @return Mono object containing a {@link List} of Stock {@link StockResponse}
     */
    public Mono<List<R>> productStock(final String productId) {
        LOGGER.debug("Fetching stock information for product: " + productId);
        return repository.findByProductIdOrderByUpc(productId)
                .switchIfEmpty(helperService.stockNotFound())
                .map(helperService::buildFromStock)
                .collectList();
    }

    /**
     * Method to lookup Database for Stock information for requested UPC
     *
     * @param upc the SKU upc
     * @return Mono object containing Stock {@link StockResponse}
     */
    public Mono<R> skuStock(final Long upc) {
        LOGGER.debug("Fetching stock information for UPC: " + upc);
        return repository.findByUpc(upc)
                .log()
                .switchIfEmpty(helperService.stockNotFound())
                .map(helperService::buildFromStock);
    }

    /**
     * Method to lookup Database for Stock details for provided ProductId and LocationId
     *
     * @param productId  the product Id
     * @param locationId the store location Id
     * @return Mono object containing a {@link List} of Stock {@link StockResponse}
     */
    public Mono<List<R>> productStockForLocation(final String productId, final Long locationId) {
        LOGGER.debug("Fetching stock information for Product: " + productId + " & location: " + locationId);
        return repository.findByProductIdOrderByUpc(productId)
                .switchIfEmpty(helperService.stockNotFound())
                .map(stock -> helperService.buildFromStock(stock, locationId))
                .collectList();
    }

    /**
     * Method to lookup Database for Stock details for provided UPC and LocationId
     *
     * @param upc        the SKU upc
     * @param locationId the store location Id
     * @return Mono<StockResponse> with only requested UPC and locationId, if present
     */
    public Mono<R> skuStockForLocation(final Long upc, final Long locationId) {
        LOGGER.debug("Fetching stock information for UPC: " + upc + " & location: " + locationId);
        return repository.findByUpc(upc)
                .switchIfEmpty(helperService.stockNotFound())
                .map(stock -> helperService.buildFromStock(stock, locationId));
    }
}
