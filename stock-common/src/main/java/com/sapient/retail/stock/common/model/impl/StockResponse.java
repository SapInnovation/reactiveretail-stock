/**
 *
 */
package com.sapient.retail.stock.common.model.impl;

import com.sapient.retail.stock.common.model.Response;

/**
 * Bean to be populated and served as API response for Stock information.
 *
 * @author ragarora
 */
public class StockResponse extends Response {

    private Long upc;

    private String productId;

    private Long locationId;

    private String locationName;

    private Long availableStock;

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
}
