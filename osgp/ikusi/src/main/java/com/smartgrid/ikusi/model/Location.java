package com.smartgrid.ikusi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;



@Entity
@Table(name = "location", schema = "public")
public class Location extends CommonsDevice implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location")
	private long idLocation;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sub_location", 
		joinColumns = @JoinColumn(name = "id_location"), 
		inverseJoinColumns = @JoinColumn(name = "id_sub_station"))
	private Collection<SubStation> substations = new ArrayList<SubStation>();
	

//	
//	@ManyToOne(cascade={CascadeType.ALL})
//	@JsonBackReference
//    @JoinColumn(name="id_location_child", insertable = true, nullable = false, updatable = false)
//    public Location parent;
//
//	
//    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
//    private Set<Location> subLocations = new HashSet<Location>();


//	public Collection<SubStation> getSubstations() {
//		return substations;
//	}
//
//
//	public void setSubstations(Collection<SubStation> substations) {
//		this.substations = substations;
//	}


	public long getIdLocation() {
		return idLocation;
	}


	public void setIdLocation(long idLocation) {
		this.idLocation = idLocation;
	}


	public Collection<SubStation> getSubstations() {
		return substations;
	}


	public void setSubstations(Collection<SubStation> substations) {
		this.substations = substations;
	}




//
//	public Location getParent() {
//		return parent;
//	}
//
//
//	public void setParent(Location parent) {
//		this.parent = parent;
//	}
//
//
//	public Set<Location> getSubLocations() {
//		return subLocations;
//	}
//
//
//	public void setSubLocations(Set<Location> subLocations) {
//		this.subLocations = subLocations;
//	}

	
}
