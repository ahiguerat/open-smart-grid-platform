package com.smartgrid.ikusi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartgrid.ikusi.model.DeviceFirmwareStatus;
import com.smartgrid.ikusi.services.DeviceFirmwareStatusServices;

@RestController
@RequestMapping(value = "/deviceFirmwareStatus", produces = { "application/json" })
public class DeviceFirmwareStatusController {
	
	@Autowired
	private DeviceFirmwareStatusServices deviceFirmwareStatusServices;
	
	@RequestMapping(value = "/saveDeviceFirmwareStatusAndSendNotification", method = RequestMethod.POST)
	public ResponseEntity<?> saveDeviceFirmwareStatusAndSendNotification(@RequestBody DeviceFirmwareStatus model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(deviceFirmwareStatusServices.saveDeviceFirmwareStatusAndSendNotification(model));						
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> add(@RequestBody DeviceFirmwareStatus model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(deviceFirmwareStatusServices.save(model));						
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<?> update(@RequestBody DeviceFirmwareStatus model) throws JsonProcessingException {
		return ResponseEntity.ok(this.deviceFirmwareStatusServices.update(model));
	}
	
	
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam long id ) {
		return ResponseEntity.ok(this.deviceFirmwareStatusServices.findById(id));
	}
	
	@RequestMapping(value = "/findByDeviceIdentification", method = RequestMethod.GET)
	public ResponseEntity<?> findByDeviceIdentification(@RequestParam String deviceIdentification ) {
		return ResponseEntity.ok(this.deviceFirmwareStatusServices.findByDeviceIdentification(deviceIdentification));
	}
	
	@RequestMapping(value = "/findLastFirmwareInstalledByDeviceAndStatus", method = RequestMethod.GET)
	public ResponseEntity<?> findLastFirmwareInstalledByDeviceAndStatus(@RequestParam String deviceIdentification, @RequestParam String status ) {
		return ResponseEntity.ok(this.deviceFirmwareStatusServices.findLastFirmwareInstalledByDeviceAndStatus(deviceIdentification, status));
	}
	
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public ResponseEntity<?> findAll( ) {
		return ResponseEntity.ok(this.deviceFirmwareStatusServices.findLastStatusFirmwareInstalledToDevices());
	}

}
