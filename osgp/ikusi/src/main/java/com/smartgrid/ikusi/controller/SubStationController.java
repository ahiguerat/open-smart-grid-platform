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
import com.smartgrid.ikusi.model.SubStation;
import com.smartgrid.ikusi.services.SubStationServices;

@RestController
@RequestMapping(value = "/substation", produces = { "application/json" })
public class SubStationController {
	@Autowired
	private SubStationServices subStationServices;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody SubStation model) {
		return ResponseEntity.ok(this.subStationServices.save(model));
	}
	

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@RequestBody SubStation model) {
		return ResponseEntity.ok(this.subStationServices.delete(model));
	}
	
	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(this.subStationServices.findAll());
	}
	
	@RequestMapping(value = "/substationNotOcupated", method = RequestMethod.POST)
	public ResponseEntity<?> getSubstationNotOcupated( ) {
		return ResponseEntity.ok(this.subStationServices.getSubstationNotOcupated());
	}
	
	@RequestMapping(value = "/findSubstationByIdAndLines", method = RequestMethod.GET)
	public ResponseEntity<?> findSubstationByIdAndLines(@RequestParam List<Long>ids, @RequestParam boolean showLines) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.subStationServices.findSubstationByIdAndLines(ids, showLines));				
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public ResponseEntity<?> edit(@RequestBody SubStation model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.subStationServices.edit(model));						
	}
	
	@RequestMapping(value = "/deleteLine", method = RequestMethod.POST)
	public ResponseEntity<?> stationDelete(@RequestBody SubStation model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.subStationServices.deleteLine(model));						
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getReport() throws DocumentException, MalformedURLException, IOException {       
    	return this.subStationServices.getReport();
    }
	
}
