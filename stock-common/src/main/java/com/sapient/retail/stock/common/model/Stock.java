package com.sapient.retail.stock.common.model;

import java.util.Map;

public interface Stock {

	public abstract Long getUpc();

	public abstract void setUpc(Long upc);

	public abstract String getPartNumber();

	public abstract void setPartNumber(String partNumber);

	public abstract String getProductId();

	public abstract void setProductId(String productId);

	public abstract String getInformationSource();

	public abstract void setInformationSource(String informationSource);

	public abstract Map<Long, StockInfo> getStock();

	public abstract void setStock(Map<Long, StockInfo> stock);

	public abstract String toString();

}