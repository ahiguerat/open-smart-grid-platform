package com.smartgrid.ikusi.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.ActivateDeviceAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.RevokeKeyRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.UpdateDeviceProtocolRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.UpdateKeyRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.model.ActivateDevice;
import com.smartgrid.ikusi.model.FilterLocation;
import com.smartgrid.ikusi.services.DeviceServices;

@RestController
@RequestMapping(value = "/device", produces = { "application/json" })
public class DeviceController {
	
	@Autowired
	private DeviceServices deviceServices;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> deviceAdd(@RequestBody Device device) {
		return ResponseEntity.ok(deviceServices.deviceAdd(device));
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseEntity<?> deviceEdit(@RequestBody Device device) {
		return ResponseEntity.ok(deviceServices.deviceUpdate(device));
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ResponseEntity<?> deviceRemove(@RequestBody String codeDevice) {
		return ResponseEntity.ok(deviceServices.deviceRemove(codeDevice));
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<?> deviceList(@RequestBody DeviceFilter filter) {
		return ResponseEntity.ok(deviceServices.deviceFind(filter));
	}	

	@RequestMapping(value = "/updateDeviceProtocol", method = RequestMethod.POST)
	public ResponseEntity<?> updateDeviceProtocol(@RequestBody UpdateDeviceProtocolRequest request) {
		return ResponseEntity.ok(deviceServices.updateDeviceProtocol(request));
	}
	
	@RequestMapping(value = "/updateKey", method = RequestMethod.POST)
	public ResponseEntity<?> updateKey(@RequestBody UpdateKeyRequest request) {
		return ResponseEntity.ok(deviceServices.updateKey(request));
	}
	
	@RequestMapping(value = "/revokeKey", method = RequestMethod.POST)
	public ResponseEntity<?> revokeKey(@RequestBody RevokeKeyRequest request) {
		return ResponseEntity.ok(deviceServices.revokeKey(request));
	}
	
	@RequestMapping(value = "/activateDevice", method = RequestMethod.POST)
	public ResponseEntity<?> activateDevice(@RequestBody ActivateDevice activateDevice) {
		return ResponseEntity.ok(deviceServices.activateDevice(activateDevice));
	}
	
	@RequestMapping(value = "/getActivateDeviceResponse", method = RequestMethod.POST)
	public ResponseEntity<?> getActivateDeviceResponse(@RequestBody ActivateDeviceAsyncRequest request) {
		return ResponseEntity.ok(deviceServices.getActivateDeviceResponse(request));
	}
	
	@RequestMapping(value = "/findDeviceNotAssoaciateStation", method = RequestMethod.GET)
	public ResponseEntity<?> findDeviceNotAssoaciateStation( ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.deviceServices.findDeviceNotAssoaciateStation());
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getReport() throws DocumentException, MalformedURLException, IOException {
       
    	return this.deviceServices.getReportDevice();
    }
	
	@RequestMapping(value = "/listByFilter", method = RequestMethod.POST)
	public ResponseEntity<?> listByStations(@RequestBody FilterLocation filter) {
		return ResponseEntity.ok(deviceServices.devicesFilters(filter));
	}

}
