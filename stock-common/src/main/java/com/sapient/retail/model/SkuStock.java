package com.sapient.retail.model;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Bean with fields mapped to MongoDB collection and its fields as mentioned.
 * @author ragarora
 */
public class SkuStock
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
	
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Long getAvailableValue() {
		return availableValue;
	}

	public void setAvailableValue(Long availableValue) {
		this.availableValue = availableValue;
	}

	/**
	 * Empty default constructor
	 */
	public SkuStock() {
		super();
	}

	/**
	 * @param locationId
	 * @param locationName
	 * @param availableValue
	 */
	public SkuStock(@Size(min = 10, message = "Location id cannot be so low") Long locationId,
			@Size(min = 3, max = 256, message = "Location name should be between 3 and 256 characters") String locationName,
			@Size(min = 0, message = "Stock value cannot be less than zero") Long availableValue) {
		super();
		this.locationId = locationId;
		this.locationName = locationName;
		this.availableValue = availableValue;
	}

}
