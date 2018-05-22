package com.sapient.retail.stock.common.model.impl;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.sapient.retail.stock.common.model.StockInfo;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
public class RetailStockInfo implements StockInfo
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
	
	//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
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
