package com.sapient.retail.stock.service.exception;

/**
 * Exception class in case Stock is not found for the given query.
 */
public class StockNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public StockNotFoundException() {
        super();
    }
}
