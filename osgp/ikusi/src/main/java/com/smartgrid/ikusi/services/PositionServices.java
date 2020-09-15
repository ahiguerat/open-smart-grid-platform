package com.smartgrid.ikusi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgrid.ikusi.protocol.entities.Position;
import com.smartgrid.ikusi.protocol.entities.OrmRtuDevice;
import com.smartgrid.ikusi.protocol.repositories.RtuDeviceRepository;

@Service
public class PositionServices {
	
	@Autowired
	private RtuDeviceRepository rtuDeviceRepository;
	
	public List<Position> getPositions(String deviceIdentification){
		OrmRtuDevice device  = this.rtuDeviceRepository.findByDeviceIdentification(deviceIdentification);
		return device.getPositions();
	}

}
