package com.sapient.retail.service.streamkafka.model;

// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class StockInfo
{
	private long locationId;
	private String locationName;
	private int availableValue;

	/**
	 * @param locationId
	 * @param locationName
	 * @param availableValue
	 */
	public StockInfo(long locationId, String locationName, int availableValue) {
		super();
		this.locationId = locationId;
		this.locationName = locationName;
		this.availableValue = availableValue;
	}
	/**
	 * Empty default constructor
	 */
	public StockInfo() {
		super();
	}

}
