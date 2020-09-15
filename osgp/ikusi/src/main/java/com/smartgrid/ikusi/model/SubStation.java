package com.smartgrid.ikusi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "sub_station", schema = "public")
public class SubStation extends CommonsDevice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sub_station")
	private long idSubStation;
	

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "line_sub_station", 
			   joinColumns = @JoinColumn(name = "id_sub_station"), 
			   inverseJoinColumns = @JoinColumn(name = "id_line"))
	private Collection<Line> lines = new ArrayList<Line>();

	public long getIdSubStation() {
		return idSubStation;
	}

	public void setIdSubStation(long idSubStation) {
		this.idSubStation = idSubStation;
	}

	public Collection<Line> getLines() {
		return lines;
	}

	public void setLines(Collection<Line> lines) {
		this.lines = lines;
	}


}
