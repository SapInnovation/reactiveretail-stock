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

    public StockService(final StockRepository repository) {
        this.repository = repository;
    }

    /**
     * Method to lookup Database for Stock information for requested Product
     *
     * @param productId
     * @return Flux<Stock> for requested Product and its all UPCs and Locations
     */
    public Mono<List<StockResponse>> productStock(final String productId) {
        LOGGER.debug("Fetching stock information for product: " + productId);
        return repository.findStocksByProductIdOrderByUpc(productId)
                .map(StockResponse::buildFromStock)
                .collectList();
    }

    /**
     * Method to lookup Database for Stock information for requested UPC
     *
     * @param upc
     * @return Flux<StockResponse> for only requested UPC, if present
     */
    public Mono<StockResponse> skuStock(final Long upc) {
        LOGGER.debug("Fetching stock information for UPC: " + upc);
        return repository.findStocksByUpc(upc)
                .map(StockResponse::buildFromStock);
    }

    /**
     * Method to lookup Database for Stock details for provided ProductId and LocationId
     *
     * @param productId
     * @param locationId
     * @return Flux<StockResponse> with only requested ProductId and locationId, if present
     */
    public Mono<List<StockResponse>> productStockForLocation(final String productId, final Long locationId) {
        LOGGER.debug("Fetching stock information for Product: " + productId + " & location: " + locationId);
        return repository.findStocksByProductAndLocationId(productId, locationId)
                .map(stock -> StockResponse.buildFromStock(stock, locationId))
                .collectList();
    }

    /**
     * Method to lookup Database for Stock details for provided UPC and LocationId
     *
     * @param upc
     * @param locationId
     * @return Mono<StockResponse> with only requested UPC and locationId, if present
     */
    public Mono<StockResponse> skuStockForLocation(final Long upc, final Long locationId) {
        LOGGER.debug("Fetching stock information for UPC: " + upc + " & location: " + locationId);
        return repository.findStocksByUpcAndLocationId(upc, locationId)
                .map(stock -> StockResponse.buildFromStock(stock, locationId));
    }
}
