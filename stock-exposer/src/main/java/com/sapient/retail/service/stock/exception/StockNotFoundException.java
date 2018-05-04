package com.sapient.retail.service.stock.exception;

/**
 * @author ragarora
 */
public class StockNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockNotFoundException(String skuId) {
        super("ProductStock not found with id " + skuId);
    }
}
