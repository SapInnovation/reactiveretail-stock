package com.sapient.retail.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
@Document(collection="stock")
public class ProductStock
{
	@NotNull
	@Size(min = 10, message = "UPC id cannot be so low")
	@Id
	private Long upc;

	@NotNull
	@Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters")
	private String productId;

	@NotNull
	@Field("stock")
	private List<SkuStock> stock;

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

	public List<SkuStock> getStock() {
		return stock;
	}

	public void setStock(List<SkuStock> stock) {
		this.stock = stock;
	}

	/**
	 * Empty default constructor
	 */
	public ProductStock() {
		super();
	}

	/**
	 * @param upc
	 * @param productId
	 * @param stock
	 */
	public ProductStock(@Size(min = 10, message = "UPC id cannot be so low") Long upc,
			@Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters") String productId,
			List<SkuStock> stock) {
		super();
		this.upc = upc;
		this.productId = productId;
		this.stock = stock;
	}

}
