package com.sapient.retail.streamkafka.web;

import com.sapient.retail.stock.common.model.Stock;

public interface StockDataController<T extends Stock> {

	void StockData(T productStock);

}