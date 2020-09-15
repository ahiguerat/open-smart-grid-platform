package com.smartgrid.ikusi.model;

import java.util.List;

public class FilterLocation {
	
	private List<Integer> location;
	private List<Integer> substation;
	private List<Integer> line;
	private List<Integer> stations;
	
	public List<Integer> getLocation() {
		return location;
	}
	public void setLocation(List<Integer> location) {
		this.location = location;
	}
	public List<Integer> getSubstation() {
		return substation;
	}
	public void setSubstation(List<Integer> substation) {
		this.substation = substation;
	}
	public List<Integer> getLine() {
		return line;
	}
	public void setLine(List<Integer> line) {
		this.line = line;
	}
	public List<Integer> getStations() {
		return stations;
	}
	public void setStations(List<Integer> stations) {
		this.stations = stations;
	}

}
