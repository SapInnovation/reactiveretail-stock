/**
 * 
 */
package com.sapient.retail.stock.common.model.impl;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
@Document(collection="stock")
public class RetailStock implements Stock
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
	@Indexed
	private String productId;
	
	@NotNull
	@Size(min = 3, max = 20, message = "Information Source should be between 3 and 20 characters")
	private String informationSource;

	@NotNull
	private Map<Long, StockInfo> stock;

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

	public String getInformationSource() {
		return informationSource;
	}

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

	public Map<Long, StockInfo> getStock() {
		return stock;
	}

	public void setStock(Map<Long, StockInfo> stock) {
		this.stock = stock;
	}

	/**
	 * @param upc
	 * @param partNumber
	 * @param productId
	 * @param informationSource
	 * @param stock
	 */
	public RetailStock(@NotNull @Size(min = 10, message = "UPC id cannot be so low") Long upc,
			@NotNull @Size(min = 3, max = 20, message = "Partnumber should be between 3 and 20 characters") String partNumber,
			@NotNull @Size(min = 3, max = 20, message = "Product id should be between 3 and 20 characters") String productId,
			@NotNull @Size(min = 3, max = 20, message = "Information Source should be between 3 and 20 characters") String informationSource,
			@NotNull Map<Long, StockInfo> stock) {
		super();
		this.upc = upc;
		this.partNumber = partNumber;
		this.productId = productId;
		this.informationSource = informationSource;
		this.stock = stock;
	}

	/**
	 * 
	 */
	public RetailStock() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Stock{" +
				"upc=" + upc +
				", partNumber='" + partNumber + '\'' +
				", productId='" + productId + '\'' +
				", informationSource='" + informationSource + '\'' +
				", stock=" + stock +
				'}';
	}
}
