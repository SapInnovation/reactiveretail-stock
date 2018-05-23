package com.sapient.retail.stock.common.model;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
public interface StockInfo
{

	public abstract String getLocationName();

	public abstract void setLocationName(String locationName);

	public abstract Long getSupply();

	public abstract void setSupply(Long supply);

	public abstract Long getDemand();

	public abstract void setDemand(Long demand);

	public abstract Long getAvailableStock();

	public abstract void setAvailableStock(Long availableStock);

	public abstract String getStockTimestamp();
	
	public abstract void setStockTimestamp(String stockTimestamp);

	public abstract String toString();
}
