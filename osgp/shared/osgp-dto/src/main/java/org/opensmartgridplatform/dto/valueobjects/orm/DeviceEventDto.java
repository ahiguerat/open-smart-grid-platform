package org.opensmartgridplatform.dto.valueobjects.orm;

import java.io.Serializable;

public class DeviceEventDto implements Serializable {
	
	private static final long serialVersionUID = 560487622939622317L;

	private String deviceIdentification;
	
	private String description;

	public DeviceEventDto() {}

	public String getDeviceIdentification() {
		return deviceIdentification;
	}

	public void setDeviceIdentification(String deviceIdentification) {
		this.deviceIdentification = deviceIdentification;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
