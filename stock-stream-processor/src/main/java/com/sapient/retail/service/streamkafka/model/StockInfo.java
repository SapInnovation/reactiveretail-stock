package com.sapient.retail.service.streamkafka.model;

import javax.validation.constraints.Size;

import com.esotericsoftware.kryo.NotNull;

// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
@Getter @Setter @ToString @Builder
public class StockInfo
{
	@NotNull
	@Size(min = 10, message = "Location id cannot be so low")
	private Long locationId;

	@NotNull
	@Size(min = 3, max=256, message = "Location name should be between 3 and 256 characters")
	private String locationName;

	@NotNull
	@Size(min = 0, message = "Stock value cannot be less than zero")
	private Long availableValue;

	
	/**
	 * Empty default constructor
	 */
	public StockInfo() {
		super();
	}


	/**
	 * @param locationId
	 * @param locationName
	 * @param availableValue
	 */
	public StockInfo(@Size(min = 10, message = "Location id cannot be so low") Long locationId,
			@Size(min = 3, max = 256, message = "Location name should be between 3 and 256 characters") String locationName,
			@Size(min = 0, message = "Stock value cannot be less than zero") Long availableValue) {
		super();
		this.locationId = locationId;
		this.locationName = locationName;
		this.availableValue = availableValue;
	}

}