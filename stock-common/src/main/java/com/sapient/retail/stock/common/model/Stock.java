package com.sapient.retail.stock.common.model;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 *
 * @author ragarora
 */
public abstract class Stock {
    @NotNull
    //@Size(min = 10, message = "UPC id cannot be so low")
    @Id
    @Min(value=10L, message = "UPC id cannot be so low")
    private Long upc;

    @NotNull
    @Size(min = 3, max = 20, message = "Partnumber should be between 3 and 20 characters")
    private String partNumber;

    @NotNull
    @Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters")
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
