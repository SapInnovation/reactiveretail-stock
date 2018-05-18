package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.exception.StockNotFoundException;
import com.sapient.retail.stock.model.StockResponse;

import reactor.core.publisher.Mono;

public interface HelperService {

	/**
	 * Return a Mono in case Stock is not available for the query.
	 *
	 * @return Mono object
	 */
	Mono<Stock> stockNotFound();

	/**
	 * Method to build {@link StockResponse} from DB response for default store.
	 * This method will throw {@link StockNotFoundException} in case stock is
	 * not available in db.
	 *
	 * @param stock the DB stock response object
	 * @return the {@link StockResponse} object
	 */
	StockResponse buildFromStock(Stock stock);

	/**
	 * Method to build {@link StockResponse} from DB response for given store.
	 * This method will throw {@link StockNotFoundException} in case stock is
	 * not available in db.
	 *
	 * @param stock the DB stock response object
	 * @return the {@link StockResponse} object
	 */
	StockResponse buildFromStock(Stock stock, Long locationId);

}