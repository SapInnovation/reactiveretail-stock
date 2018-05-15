package com.sapient.retail.service.streamkafka.model;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.esotericsoftware.kryo.NotNull;

// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
@Getter @Setter @ToString @Builder
@Document(collection="stock")
public class ProductStockInfo
{
	@NotNull
	@Size(min = 10, message = "UPC id cannot be so low")
	@Id
	private long upc;

	@NotNull
	@Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters")
	private String productId;

	@NotNull
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
