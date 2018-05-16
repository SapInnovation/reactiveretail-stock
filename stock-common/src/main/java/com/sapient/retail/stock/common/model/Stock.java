package com.sapient.retail.stock.common.model;


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
public class Stock
{
	@NotNull
	@Size(min = 10, message = "UPC id cannot be so low")
	@Id
	private Long upc;

	@NotNull
	@Size(min = 3, max = 20, message = "Partnumber should be between 3 and 20 characters")
	private String partNumber;

	@NotNull
	@Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters")
	private String productId;

	@NotNull
	@Field("stock")
	private List<StockInfo> stock;

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
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

	public List<StockInfo> getStock() {
		return stock;
	}

	public void setStock(List<StockInfo> stock) {
		this.stock = stock;
	}

	/**
	 * Empty default constructor
	 */
	public Stock() {
		super();
	}

	/**
	 * @param upc
	 * @param partNumber
	 * @param productId
	 * @param stock
	 */
	public Stock(@NotNull @Size(min = 10, message = "UPC id cannot be so low") Long upc,
			@NotNull @Size(min = 3, max = 20, message = "Partnumber should be between 3 and 20 characters") String partNumber,
			@NotNull @Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters") String productId,
			@NotNull List<StockInfo> stock) {
		super();
		this.upc = upc;
		this.partNumber = partNumber;
		this.productId = productId;
		this.stock = stock;
	}

}
