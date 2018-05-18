/**
 *
 */
package com.sapient.retail.stock.service.model;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;
import com.sapient.retail.stock.service.exception.StockNotFoundException;

/**
 * Bean to be populated and served as API response for Stock information.
 *
 * @author ragarora
 */
public class StockResponse {

    private Long upc;

    private String productId;

    private Long locationId;

    private String locationName;

    private Long availableStock;

    /**
     * Empty default constructor
     */
    public StockResponse() {
        super();
    }

    /**
     * @param upc
     * @param productId
     * @param locationId
     * @param locationName
     * @param availableStock
     */
    public StockResponse(Long upc, String productId, Long locationId, String locationName, Long availableStock) {
        super();
        this.upc = upc;
        this.productId = productId;
        this.locationId = locationId;
        this.locationName = locationName;
        this.availableStock = availableStock;
    }

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Long availableStock) {
        this.availableStock = availableStock;
    }

    public static StockResponse buildFromStock(final Stock stock) {
        return buildFromStock(stock, 10001L);
    }

    public static StockResponse buildFromStock(final Stock stock, final Long locationId) {
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
