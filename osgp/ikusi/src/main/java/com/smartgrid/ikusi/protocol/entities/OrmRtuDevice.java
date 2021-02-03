package com.smartgrid.ikusi.protocol.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class OrmRtuDevice extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column(unique = true, nullable = false, length = 40)
	private String deviceIdentification;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "ormRtuDevice", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Position> positions;

	public OrmRtuDevice() {}
	
	public OrmRtuDevice(String deviceIdentification) {
		super();
		this.deviceIdentification = deviceIdentification;
	}

	public String getDeviceIdentification() {
		return deviceIdentification;
	}

	public void setDeviceIdentification(String deviceIdentification) {
		this.deviceIdentification = deviceIdentification;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
	
}