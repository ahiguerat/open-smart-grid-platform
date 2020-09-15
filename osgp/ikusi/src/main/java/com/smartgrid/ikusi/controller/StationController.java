package com.smartgrid.ikusi.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.model.Station;
import com.smartgrid.ikusi.services.StationService;

@RestController
@RequestMapping(value = "/station", produces = { "application/json" })
public class StationController {

	@Autowired
	private StationService stationService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<?> stationList( ) {
		return ResponseEntity.ok(this.stationService.list());
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> stationAdd(@RequestBody Station model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.stationService.save(model));						
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> stationDelete(@RequestBody Station model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.stationService.delete(model));						
	}
	
	@RequestMapping(value = "/stationNotAssociatedToLine", method = RequestMethod.GET)
	public ResponseEntity<?> stationNotAssociatedToLine( ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.stationService.getStationNotAssociated());				
	}
	
	@RequestMapping(value = "/findStationByIdAndDevices", method = RequestMethod.GET)
	public ResponseEntity<?> findStationByIdAndDevices(@RequestParam List<Long> ids, @RequestParam boolean showDevices  ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.stationService.findStationByIdAndDevices(ids, showDevices));				
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getReport() throws DocumentException, MalformedURLException, IOException {       
    	return this.stationService.getReport();
    }
}
