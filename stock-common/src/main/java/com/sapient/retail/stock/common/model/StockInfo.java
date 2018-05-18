package com.sapient.retail.stock.common.model;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
public class StockInfo
{

	@NotNull
	@Size(min = 3, max=256, message = "Location name should be between 3 and 256 characters")
	private String locationName;

	@Nullable
	@Size(min = 0, message = "Supply value cannot be less than zero")
	private Long supply;
	
	@Nullable
	@Size(min = 0, message = "Demand value cannot be less than zero")
	private Long demand;
	
	@Nullable
	@Size(min = 0, message = "Available Stock value cannot be less than zero")
	private Long availableStock;
	
	private String stockTimestamp;

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Long getSupply() {
		return supply;
	}

	public void setSupply(Long supply) {
		this.supply = supply;
	}

	public Long getDemand() {
		return demand;
	}

	public void setDemand(Long demand) {
		this.demand = demand;
	}

	public Long getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Long availableStock) {
		this.availableStock = availableStock;
	}

	public String getStockTimestamp() {
		return stockTimestamp;
	}

	public void setStockTimestamp(String stockTimestamp) {
		this.stockTimestamp = stockTimestamp;
	}

	/**
	 * @param locationName
	 * @param supply
	 * @param demand
	 * @param availableStock
	 * @param stockTimestamp
	 */
	public StockInfo(
			@NotNull @Size(min = 3, max = 256, message = "Location name should be between 3 and 256 characters") String locationName,
			@Size(min = 0, message = "Supply value cannot be less than zero") Long supply,
			@Size(min = 0, message = "Demand value cannot be less than zero") Long demand,
			@Size(min = 0, message = "Available Stock value cannot be less than zero") Long availableStock,
			String stockTimestamp) {
		super();
		this.locationName = locationName;
		this.supply = supply;
		this.demand = demand;
		this.availableStock = availableStock;
		this.stockTimestamp = stockTimestamp;
	}

	/**
	 * 
	 */
	public StockInfo() {
		super();
	}

	@Override
	public String toString() {
		return "StockInfo{" +
				"locationName='" + locationName + '\'' +
				", supply=" + supply +
				", demand=" + demand +
				", availableStock=" + availableStock +
				", stockTimestamp='" + stockTimestamp + '\'' +
				'}';
	}
}
