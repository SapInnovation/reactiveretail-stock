package com.sapient.retail.streamkafka.listener;

import com.sapient.retail.stock.common.model.Stock;

public interface StockDataListener<T extends Stock> {

	void handleStockDataFromTopic(T newStockDetails);

}
