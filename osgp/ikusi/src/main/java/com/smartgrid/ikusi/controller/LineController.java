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
import com.smartgrid.ikusi.model.Line;
import com.smartgrid.ikusi.services.LineServices;

@RestController
@RequestMapping(value = "/line", produces = { "application/json" })
public class LineController {

	@Autowired
	private LineServices lineServices;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<?> deviceList( ) {
		return ResponseEntity.ok(this.lineServices.getList());
	}	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> stationAdd(@RequestBody Line line) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.lineServices.save(line));						
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> lineDelete(@RequestBody Line line) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.lineServices.delete(line));						
	}
	
	@RequestMapping(value = "/listLineNotOcupated", method = RequestMethod.POST)
	public ResponseEntity<?> getLineNotOcupated( ) {
		return ResponseEntity.ok(this.lineServices.getLineNotOcupated());
	}
	
	@RequestMapping(value = "/findLineByIdAndStations", method = RequestMethod.GET)
	public ResponseEntity<?> findLineByIdAndStations(@RequestParam List<Long>ids, @RequestParam boolean showStations) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.lineServices.findLineByIdAndStations(ids, showStations));				
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public ResponseEntity<?> stationEdit(@RequestBody Line line) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.lineServices.edit(line));						
	}
	
	@RequestMapping(value = "/deleteStation", method = RequestMethod.POST)
	public ResponseEntity<?> stationDelete(@RequestBody Line line) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.lineServices.deleteStation(line));						
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getReport() throws DocumentException, MalformedURLException, IOException {       
    	return this.lineServices.getReport();
    }
}
