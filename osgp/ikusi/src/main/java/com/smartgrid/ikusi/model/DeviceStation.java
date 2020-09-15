package com.smartgrid.ikusi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.smartgrid.ikusi.protocol.entities.Position;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "device_station_ct", schema = "public")
public class DeviceStation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "device_identification", unique = true)
	private String deviceIdentification;
	
	@Column(name = "gateway_device_id")
	private Integer gatewayDeviceId;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_station_ct", nullable = false)
	@JsonBackReference
    private Station station;
	

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@CreationTimestamp 
    @Column(name = "creation_time", nullable = false, updatable = false)  
    private Date created;
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@UpdateTimestamp
    @Column(name = "modify_time", insertable = false)  
    private Date modified;
		
	@Transient	
    private String stationName;

	@Transient
    private String stationCode;
		
	@Transient
    private Long stationId;
		
	@Transient
    private String name;
	
	@Transient
    private String code;
	
	@Transient
	private org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device device;
	
	@Transient
	private HashMap<String,Object> event;
	
	@Transient
	private List<Position> positions;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDeviceIdentification() {
		return deviceIdentification;
	}

	public void setDeviceIdentification(String deviceIdentification) {
		this.deviceIdentification = deviceIdentification;
	}

	public Integer getGatewayDeviceId() {
		return gatewayDeviceId;
	}

	public void setGatewayDeviceId(Integer gatewayDeviceId) {
		this.gatewayDeviceId = gatewayDeviceId;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
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

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getName() {
		return this.getDeviceIdentification();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.getDeviceIdentification();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device getDevice() {
		return device;
	}

	public void setDevice(org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device device) {
		this.device = device;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public HashMap<String, Object> getEvent() {
		return event;
	}

	public void setEvent(HashMap<String, Object> event) {
		this.event = event;
	}

}
