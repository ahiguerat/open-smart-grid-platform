package com.smartgrid.ikusi.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceFilter;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.FindDevicesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgrid.ikusi.protocol.entities.Position;
import com.smartgrid.ikusi.reports.LocationReport;
import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.config.NotFoundException;
import com.smartgrid.ikusi.model.DeviceStation;
import com.smartgrid.ikusi.model.Line;
import com.smartgrid.ikusi.model.Location;
import com.smartgrid.ikusi.model.Station;
import com.smartgrid.ikusi.model.SubStation;
import  com.smartgrid.ikusi.repository.LocationRepository;

@Service
public class LocationServices {
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private PositionServices positionServices;
	
	@Autowired
	private DeviceServices deviceServices;
	
	@Autowired
	private LocationReport locationReport;

	public List<Location> getLocationNotOcupated(){
		return this.locationRepository.getLocationNotOcupated();
	}
	
	public List<Location> findAll(){
		return this.locationRepository.findByOrderByIdLocationDesc();
	}
	
	public List<Location> findAllComplete(){
		List<Location> locations = this.locationRepository.findByOrderByIdLocationDesc();
		for (Location record : ListUtils.emptyIfNull(locations)) {
			Iterator<SubStation> item = record.getSubstations().iterator();
			while (item.hasNext()) {
				item.next();
			}
			if(record.getSubstations()!=null) {
				for(SubStation substation: record.getSubstations()) {
					Iterator<Line> itemLine = substation.getLines().iterator();
					while (itemLine.hasNext()) {
						itemLine.next();
					}
					if(substation.getLines() !=null) {
						for(Line line: substation.getLines()) {
							Iterator<Station> itemStation = line.getStations().iterator();
							while (itemStation.hasNext()) {
								itemStation.next();
							}
							if(line.getStations() != null) {
								for(Station station : line.getStations()) {
									Iterator<DeviceStation> deviceStation = station.getDevices().iterator();
									while (deviceStation.hasNext()) {
										DeviceStation tmp = deviceStation.next();
										
										DeviceFilter filter = new DeviceFilter();
										filter.setDeviceIdentification(tmp.getDeviceIdentification());										
										FindDevicesResponse response = this.deviceServices.deviceFind(filter);
										Device device = response != null && response.getDevices()!=null && response.getDevices().size() > 0 ? response.getDevices().get(0):null;
										List<Position> positions = new ArrayList<Position>();
										try {
											positions = this.positionServices.getPositions(tmp.getDeviceIdentification());
										}catch(Exception e) {
											System.out.println(e.toString());
										}																			
										tmp.setDevice(device);
										tmp.setPositions(positions);									
									}									
								}
							}
						}
					}
					
				}				
			}						
		}
		
		return locations;
	}
	
	public Location save(Location location) {
		this.locationRepository.save(location);
		return location;
	}
	
	public Location delete(Location location) {
		this.locationRepository.deleteById(location.getIdLocation());
		return location;
	}
	
	public List<Location> findLocationByIdAndSubstations(List<Long> ids, boolean showSubstations) {
		List<Location> locations = this.locationRepository.findAllById(ids);
		if (showSubstations) {
			for (Location record : ListUtils.emptyIfNull(locations)) {
				Iterator<SubStation> item = record.getSubstations().iterator();
				while (item.hasNext()) {
					item.next();
				}
			}
		}
		return locations;
	}

	public Location edit(Location location) {
		return this.locationRepository.findById(location.getIdLocation()).map(r -> {
			r.setName(location.getName());
			r.setCode(location.getCode());
			r.setDescription(location.getDescription());
			Iterator<SubStation> item = r.getSubstations().iterator();
			while (item.hasNext()) {
				item.next();
				
			}
			if (location.getSubstations() != null) {
				if (r.getSubstations() == null)
					r.setSubstations(new ArrayList<SubStation>());
				r.getSubstations().addAll(location.getSubstations());
			}
			return this.locationRepository.save(r);

		}).orElseThrow(() -> new NotFoundException("Substation not found with id " + location.getIdLocation()));
	}

	public Location deleteSubstation(Location location) {
		return this.locationRepository.findById(location.getIdLocation()).map(r -> {
			r.setName(location.getName());
			r.setCode(location.getCode());
			r.setDescription(location.getDescription());
			Iterator<SubStation> item = r.getSubstations().iterator();
			while (item.hasNext()) {
				item.next();
				
			}
			if (location.getSubstations() != null && r.getSubstations() != null) {
				List<SubStation> sb = r.getSubstations().stream()
						.filter(record-> location.getSubstations().stream()
								.filter(pre->pre.getIdSubStation() == record.getIdSubStation()).collect(Collectors.toList()).size() < 1)
						.collect(Collectors.toList());
				r.setSubstations(sb);
			}
			return this.locationRepository.save(r);

		}).orElseThrow(() -> new NotFoundException("Substation not found with id " + location.getIdLocation()));
	}

	public byte[] getReport() throws DocumentException, IOException {
		return this.locationReport.getFile(this.findAll(), "LOCATION REPORTS");
	}
	
}
