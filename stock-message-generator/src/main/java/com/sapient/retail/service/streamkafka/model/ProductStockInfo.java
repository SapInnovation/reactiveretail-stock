package com.sapient.retail.service.streamkafka.model;

import java.util.List;

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
	private Long upc;

	@NotNull
	@Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters")
	private String productId;

	@NotNull
	@Field("stock")
	private List<StockInfo> stock;

	/**
	 * Empty default constructor
	 */
	public ProductStockInfo() {
		super();
	}

	/**
	 * @param upc
	 * @param productId
	 * @param stock
	 */
	public ProductStockInfo(@Size(min = 10, message = "UPC id cannot be so low") Long upc,
			@Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters") String productId,
			List<StockInfo> stock) {
		super();
		this.upc = upc;
		this.productId = productId;
		this.stock = stock;
	}
	
}
