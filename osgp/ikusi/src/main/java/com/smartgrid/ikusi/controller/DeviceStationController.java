package com.smartgrid.ikusi.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartgrid.ikusi.model.DeviceStation;
import com.smartgrid.ikusi.services.DeviceStationServices;

@RestController
@RequestMapping(value = "/deviceStation", produces = { "application/json" })
public class DeviceStationController {
	
	@Autowired
	private DeviceStationServices deviceStationServices;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody DeviceStation model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.deviceStationServices.save(model));						
	}
		
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ResponseEntity<?> deviceModelRemove(@RequestBody DeviceStation model) {		
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.deviceStationServices.delete(model));		
	}
	
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public ResponseEntity<?> deviceModelFindAll( ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.deviceStationServices.getList());				
	}
	


}
