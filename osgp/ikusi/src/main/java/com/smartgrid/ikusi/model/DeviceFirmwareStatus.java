package com.smartgrid.ikusi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "device_firmware_status", schema = "public")
public class DeviceFirmwareStatus implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_device_firmware_status")
	private long idDeviceFirmwareStatus;
	
	@Column(name = "device_identification")
	private String  deviceIdentification;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "message")
	private String message;
	
	@Column(name = "network_address")
	private String networkAddress;
	
	@Column(name = "firmware")
	private String firmware;
	
	//@JsonIgnore
	@ApiModelProperty(hidden = true)
	@CreationTimestamp
    @Column(name = "creation_time", nullable = false, updatable = false)  
	private Date created;
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@UpdateTimestamp
    @Column(name = "modify_time", insertable = false)  
	private Date modified;

	public long getIdDeviceFirmwareStatus() {
		return idDeviceFirmwareStatus;
	}

	public void setIdDeviceFirmwareStatus(long idDeviceFirmwareStatus) {
		this.idDeviceFirmwareStatus = idDeviceFirmwareStatus;
	}

	public String getDeviceIdentification() {
		return deviceIdentification;
	}

	public void setDeviceIdentification(String deviceIdentification) {
		this.deviceIdentification = deviceIdentification;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getNetworkAddress() {
		return networkAddress;
	}

	public void setNetworkAddress(String networkAddres) {
		this.networkAddress = networkAddres;
	}

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

}
