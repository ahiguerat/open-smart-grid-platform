package com.smartgrid.ikusi.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "station_ct", schema = "public")
public class Station extends CommonsDevice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_station_ct")
	private long idStation;

	
	@OneToMany(mappedBy = "station", fetch = FetchType.LAZY)
	@JsonManagedReference
	//@OrderBy("station")
    private Set<DeviceStation> devices = new HashSet<DeviceStation>();

	public long getIdStation() {
		return idStation;
	}

	public void setIdStation(long idStation) {
		this.idStation = idStation;
	}

	public Set<DeviceStation> getDevices() {
		return devices;
	}

	public void setDevices(Set<DeviceStation> devices) {
		this.devices = devices;
	}



}
