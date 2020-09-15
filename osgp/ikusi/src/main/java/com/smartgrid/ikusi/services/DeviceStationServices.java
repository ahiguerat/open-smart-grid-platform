package com.smartgrid.ikusi.services;

import java.util.List;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgrid.ikusi.model.DeviceStation;
import com.smartgrid.ikusi.model.FilterLocation;
import com.smartgrid.ikusi.repository.DeviceStationRepository;

@Service
public class DeviceStationServices {
	
	
	@Autowired
	private DeviceStationRepository deviceStationRepository;
	
	public DeviceStation save(DeviceStation model) {
		return this.deviceStationRepository.save(model);
	}
	
	public List<DeviceStation> getList(){
		List<DeviceStation> deviceStations = this.deviceStationRepository.findAll();				
		for(DeviceStation ds: ListUtils.emptyIfNull(deviceStations)) {
			ds.setStationCode(ds.getStation().getCode());
			ds.setStationId(ds.getStation().getIdStation());
			ds.setStationName(ds.getStation().getName());
			ds.setCode(ds.getDeviceIdentification());
			ds.setName(ds.getDeviceIdentification());
		}	
		
		return deviceStations;
	}
	
	public DeviceStation delete(DeviceStation model) {
		this.deviceStationRepository.deleteById(model.getId());
		return model;
	}
	
	public Long deleteByDeviceIdentification(String deviceIdentification) {
		return this.deviceStationRepository.deleteByDeviceIdentification(deviceIdentification);
	}
	
	public List<DeviceStation> getListByFilters(FilterLocation filter){
		if(filter.getLocation() != null && filter.getLocation().size() > 0 &&
			filter.getSubstation() !=null && filter.getSubstation().size() > 0 &&	
			filter.getLine() != null && filter.getLine().size() > 0 && 
			filter.getStations() != null && filter.getStations().size() > 0) {
			return this.deviceStationRepository.byLocationAndSubstationAndLineAndStation(filter.getLocation(),
					filter.getSubstation(), filter.getLine(), filter.getStations());			
		}else if (filter.getLocation() != null && filter.getLocation().size() > 0 &&
				filter.getSubstation() !=null && filter.getSubstation().size() > 0 &&	
				filter.getLine() != null && filter.getLine().size() > 0 ) {
			return this.deviceStationRepository.byLocationAndSubstationAndLine(filter.getLocation(),
					filter.getSubstation(), filter.getLine());	
		}else if (filter.getLocation() != null && filter.getLocation().size() > 0 &&
				filter.getSubstation() !=null && filter.getSubstation().size() > 0 ) {
			return this.deviceStationRepository.byLocationAndSubstation(filter.getLocation(), filter.getSubstation());	
		}else if (filter.getLocation() != null && filter.getLocation().size() > 0 ) {
			return this.deviceStationRepository.byLocation(filter.getLocation());	
		}
		return this.deviceStationRepository.findAll();
		
	}
	
}
