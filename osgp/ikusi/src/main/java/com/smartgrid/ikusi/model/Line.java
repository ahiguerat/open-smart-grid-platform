package com.smartgrid.ikusi.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "line", schema = "public")
public class Line extends CommonsDevice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_line")
	private long idLine;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "line_ct", 
			   joinColumns = @JoinColumn(name = "id_line"), 
			   inverseJoinColumns = @JoinColumn(name = "id_station_ct"))
	private Collection<Station> stations;
	

	public long getIdLine() {
		return idLine;
	}

	public void setIdLine(long idLine) {
		this.idLine = idLine;
	}

	public Collection<Station> getStations() {
		return stations;
	}

	public void setStations(Collection<Station> stations) {
		this.stations = stations;
	}

}
