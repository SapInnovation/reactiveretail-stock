package com.sapient.retail.stock.common.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 *
 * @author ragarora
 */
public abstract class Stock {
    @NotNull
    @Id
    protected Long upc;

    @NotNull
    private String partNumber;

    @NotNull
    @Indexed
    private String productId;

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "upc=" + upc +
                ", partNumber='" + partNumber + '\'' +
                ", productId='" + productId +
                '}';
    }
}
