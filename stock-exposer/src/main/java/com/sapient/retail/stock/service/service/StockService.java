package com.sapient.retail.stock.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.repository.StockRepository;

import reactor.core.publisher.Flux;

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
    public Flux<Stock> skuStock(final Long upc) {
        LOGGER.debug("Fetching stock information for UPC: " + upc);
        return repository.findStocksByUpc(upc);
    }
    
    /**
     * Method to lookup Database for Stock details for provided UPC and LocationId
     * @param upc
     * @param locationId
     * @return Flux<Stock> with only requested UPC and locationId, if present
     */
    public Flux<Stock> skuStockForLocation(final Long upc, final Long locationId) {
        LOGGER.debug("Fetching stock information for UPC: " + upc + " & location: " + locationId);
        return repository.findStocksByUpcAndLocationId(upc, locationId)
        		.filter(stock -> 
        			stock.getStock()
        			.removeIf(location -> 
        				!location.getLocationId().equals(locationId)));
    }
}
