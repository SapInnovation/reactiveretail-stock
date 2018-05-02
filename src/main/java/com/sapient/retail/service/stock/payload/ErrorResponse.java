package com.sapient.retail.service.stock.payload;

/**
 * @author ragarora
 */
public class ErrorResponse {

	private String key;
	private String message;

    /**
	 * @param key
	 * @param message
	 */
	public ErrorResponse(String key, String message) {
		super();
		this.key = key;
		this.message = message;
	}

	public ErrorResponse(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
