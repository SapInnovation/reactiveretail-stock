package com.sapient.retail.stock.service.exception;

/**
 * @author ragarora
 */
public class StockNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockNotFoundException(String skuId) {
        super("Stock not found with id " + skuId);
    }
}
