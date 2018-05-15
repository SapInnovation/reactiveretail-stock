package com.sapient.retail.service.streamkafka.model;

// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class Stock
{
	private String stockmessage;

	/**
	 * @param stockmessage
	 */
	public Stock(String stockmessage) {
		super();
		this.stockmessage = stockmessage;
	}

	/**
	 * Empty default constructor 
	 */
	public Stock() {
		super();
	}
}
