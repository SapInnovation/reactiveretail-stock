package com.sapient.retail.service.streamkafka.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
@Document(collection="stock")
public class ProductStockInfo
{
	@Id
	private long upc;
	private String productId;

	@Field("stock")
	private StockInfo[] stock;

	/**
	 * @param upc
	 * @param productId
	 * @param stock
	 */
	public ProductStockInfo(long upc, String productId, StockInfo[] stock) {
		super();
		this.upc = upc;
		this.productId = productId;
		this.stock = stock;
	}

	/**
	 * Empty default constructor
	 */
	public ProductStockInfo() {
		super();
	}
	
}
