package org.opensmartgridplatform.dto.valueobjects.orm;

import java.io.Serializable;
import java.util.List;

public class RtuDeviceDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2196653168046111111L;

	private String deviceIdentification;
	
	private List<PositionDto> positions;
	
	private String ip;

	public RtuDeviceDto() {}
	
	public RtuDeviceDto(String deviceIdentification, List<PositionDto> positions) {
		this.positions = positions;
		this.deviceIdentification = deviceIdentification;
	}

	public String getDeviceIdentification() {
		return deviceIdentification;
	}

	public void setDeviceIdentification(String deviceIdentification) {
		this.deviceIdentification = deviceIdentification;
	}

	public List<PositionDto> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionDto> positions) {
		this.positions = positions;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
