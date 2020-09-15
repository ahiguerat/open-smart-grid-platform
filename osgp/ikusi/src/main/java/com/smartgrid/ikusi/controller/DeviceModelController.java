package com.smartgrid.ikusi.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.DeviceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.services.DeviceModelServices;

@RestController
@RequestMapping(value = "/deviceModel", produces = { "application/json" })
public class DeviceModelController {

	@Autowired
	DeviceModelServices deviceModelServices;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> deviceModelAdd(@RequestBody DeviceModel model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(deviceModelServices.deviceModelAdd(model));						
	}
	
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ResponseEntity<?> deviceModelChange(@RequestBody DeviceModel model) {
		return ResponseEntity.ok(deviceModelServices.deviceModelChange(model));
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ResponseEntity<?> deviceModelRemove(@RequestBody DeviceModel model) {		
		return ResponseEntity.status(HttpStatus.OK)
				 .body(deviceModelServices.deviceModelRemove(model));		
	}
	
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public ResponseEntity<?> deviceModelFindAll( ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(deviceModelServices.deviceModelFindAll());				
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getReport() throws DocumentException, MalformedURLException, IOException {
       
    	return this.deviceModelServices.getReportDeviceModle();
    }
}
