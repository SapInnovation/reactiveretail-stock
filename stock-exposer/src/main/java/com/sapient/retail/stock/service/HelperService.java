package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.exception.StockNotFoundException;
import com.sapient.retail.stock.model.StockResponse;

import reactor.core.publisher.Mono;

public interface HelperService {

	/**
	 * Return a Mono in case Stock is not available for the query.
	 *
	 * @return Mono object
	 */
	Mono<RetailStock> stockNotFound();

	/**
	 * Method to build {@link StockResponse} from DB response for default store.
	 * This method will throw {@link StockNotFoundException} in case stock is
	 * not available in db.
	 *
	 * @param stock the DB stock response object
	 * @return the {@link StockResponse} object
	 */
	StockResponse buildFromStock(RetailStock retailStock);

	/**
	 * Method to build {@link StockResponse} from DB response for given store.
	 * This method will throw {@link StockNotFoundException} in case stock is
	 * not available in db.
	 *
	 * @param stock the DB stock response object
	 * @return the {@link StockResponse} object
	 */
	StockResponse buildFromStock(RetailStock retailStock, Long locationId);

}