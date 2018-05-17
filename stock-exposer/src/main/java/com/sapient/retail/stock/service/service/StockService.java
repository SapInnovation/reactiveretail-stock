package com.sapient.retail.stock.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.repository.StockRepository;
import com.sapient.retail.stock.service.model.StockResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StockService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final StockRepository repository;

    public StockService(final StockRepository repository) {
        this.repository = repository;
    }

    /**
     * Method to lookup Database for Stock information for requested Product
     * @param productId
     * @return Flux<Stock> for requested Product and its all UPCs and Locations
     */
    public Flux<Stock> productStock(final String productId) {
        LOGGER.debug("Fetching stock information for product: " + productId);
        return repository.findStocksByProductIdOrderByUpc(productId);
    }

    /**
     * Method to lookup Database for Stock information for requested UPC
     * @param upc
     * @return Flux<Stock> for only requested UPC, if present
     */
    public Mono<Stock> skuStock(final Long upc) {
        LOGGER.debug("Fetching stock information for UPC: " + upc);
        return repository.findStocksByUpc(upc);
    }

    /**
     * Method to lookup Database for Stock details for provided ProductId and LocationId
     * @param productId
     * @param locationId
     * @return Flux<Stock> with only requested ProductId and locationId, if present
     */
    public Flux<Stock> productStockForLocation(final String productId, final Long locationId) {
        LOGGER.debug("Fetching stock information for Product: " + productId + " & location: " + locationId);
        /*return repository.findStocksByProductAndLocationId(productId, locationId)
        		.filter(stock -> 
        			stock.getStock()
        			.removeIf(location -> 
        				!location.getLocationId().equals(locationId)));*/
        return repository.findStocksByProductAndLocationId(productId, locationId)
        		.filter(stock -> 
        			stock.getStock().keySet().removeIf(location -> !stock.getStock().containsKey(locationId)));
    }
    
    /**
     * Method to lookup Database for Stock details for provided UPC and LocationId
     * @param upc
     * @param locationId
     * @return Mono<Stock> with only requested UPC and locationId, if present
     */
    public Mono<Stock> skuStockForLocation(final Long upc, final Long locationId) {
        LOGGER.debug("Fetching stock information for UPC: " + upc + " & location: " + locationId);
        /*return repository.findStocksByUpcAndLocationId(upc, locationId)
        		.filter(stock -> 
        			stock.getStock()
        			.removeIf(location -> 
        				!location.getLocationId().equals(locationId)));*/
        return repository.findStocksByUpcAndLocationId(upc, locationId)
        		.filter(stock -> 
        			stock.getStock().keySet().removeIf(location -> !stock.getStock().containsKey(locationId)));
    }
}
