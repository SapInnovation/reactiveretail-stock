package com.sapient.retail.stock.common.model.impl;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Document;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;

@Document(collection = "stock")
public class RetailStock extends Stock {

	@NotNull
    private String informationSource;

    @NotNull
    private Map<Long, StockInfo> stock;
    
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
    
    @Override
    public String toString() {
    	return "Stock{" +
                "upc=" + getUpc() +
                ", partNumber='" + getPartNumber() + '\'' +
                ", productId='" + getProductId() + '\'' +
                ", informationSource='" + informationSource + '\'' +
                ", stock=" + stock +
                '}';
    }
}
