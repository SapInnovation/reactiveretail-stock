package com.sapient.retail.service.stock.db.beans;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ragarora
 */
@Document(collection = "productStock")
public class ProductStock {
    @Id
    private String id;

    @NotBlank
    @Size(max = 140)
    private String productId;

    @Size(max = 140)
    private String location;
    
    @Min(value = 0, message = "Stock for a product cannot be less than 0")
    private Integer availableStock;

    public ProductStock() {

    }

    public ProductStock(String productId, Integer availableStock, String location) {
        this.id = id;
        this.productId = productId;
        this.availableStock = availableStock;
        this.location = location;
    }

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
