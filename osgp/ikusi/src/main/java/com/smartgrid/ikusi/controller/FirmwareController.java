package com.smartgrid.ikusi.controller;

import java.util.HashMap;

import javax.xml.datatype.DatatypeConfigurationException;

import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.ChangeFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.DeviceFirmware;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Firmware;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.SwitchFirmwareRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartgrid.ikusi.services.FirmwareServices;

@RestController
@RequestMapping(value = "/firmware", produces = { "application/json" })
public class FirmwareController {

	@Autowired
	private FirmwareServices firmwareServices;
	

	@RequestMapping(value = "/saveCurrentDeviceFirmware", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public ResponseEntity<?> saveCurrentDeviceFirmware(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart("deviceFirmware") DeviceFirmware deviceFirmware) throws DatatypeConfigurationException {
		try {
			if(file!=null) {				
				deviceFirmware.getFirmware().setFile(file.getBytes());;
			}
			
		} catch (Exception e) {

		}
		return ResponseEntity.ok(firmwareServices.saveCurrentDeviceFirmware(deviceFirmware));

	}

	@RequestMapping(value = "/addFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public ResponseEntity<?> addFile(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart("firmware") Firmware firmware) {
		try {
			if(file!=null) {				
				firmware.setFile(file.getBytes());
			}
			
		} catch (Exception e) {

		}
		return ResponseEntity.ok(firmwareServices.addFile(firmware));

	}
	@RequestMapping(value = "/changeFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public ResponseEntity<?> changeFile(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart("firmware") ChangeFirmwareRequest request) {
		try {
			if(file!=null) {
				request.getFirmware().setFile(file.getBytes());
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return ResponseEntity.ok(firmwareServices.changeFile(request));

	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> add(@RequestBody Firmware firmware) {
		return ResponseEntity.ok(firmwareServices.add(firmware));
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseEntity<?> edit(@RequestBody Firmware firmware) {
		return ResponseEntity.ok(firmwareServices.change(firmware));
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ResponseEntity<?> deviceRemove(@RequestBody Firmware firmware) {
		return ResponseEntity.ok(firmwareServices.remove(firmware));
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public ResponseEntity<?> findAll(@RequestBody Firmware firmware) {
		return ResponseEntity.ok(firmwareServices.findAll(firmware));
	}

	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public ResponseEntity<?> deviceList(@RequestBody Firmware firmware) {
		return ResponseEntity.ok(firmwareServices.find(firmware));
	}
	
	@RequestMapping(value = "/updateFirmware", method = RequestMethod.POST)
	public ResponseEntity<?> updateFirmware(@RequestBody HashMap<String, String> model) {
		return ResponseEntity.ok(firmwareServices.updateFirmware(model));
	}
	
	@RequestMapping(value = "/switchFirmware", method = RequestMethod.POST)
	public ResponseEntity<?> switchFirmware(@RequestBody SwitchFirmwareRequest request) {
		return ResponseEntity.ok(firmwareServices.switchFirmware(request));
	}
	
	@RequestMapping(value = "/deviceFirmwareHistory", method = RequestMethod.GET)
	public ResponseEntity<?> deviceFirmwareHistory(@RequestParam String deviceIdentification ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(firmwareServices.deviceFirmwareHistory(deviceIdentification));				
	}
	
	@RequestMapping(value = "/getDeviceFirmwareInstalled", method = RequestMethod.GET)
	public ResponseEntity<?> getDeviceFirmwareInstalled(@RequestParam String deviceIdentification, @RequestParam String manufacturer, @RequestParam String model ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(firmwareServices.getDeviceFirmwareInstalled(deviceIdentification, manufacturer, model));				
	}
}
