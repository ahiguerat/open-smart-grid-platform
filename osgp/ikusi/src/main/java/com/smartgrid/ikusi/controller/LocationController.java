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
import com.smartgrid.ikusi.model.Location;
import com.smartgrid.ikusi.services.LocationServices;

@RestController
@RequestMapping(value = "/location", produces = { "application/json" })
public class LocationController {
	@Autowired
	private LocationServices locationServices;
	

	@RequestMapping(value = "/locationNotOcupated", method = RequestMethod.POST)
	public ResponseEntity<?> getLocationNotOcupated( ) {
		return ResponseEntity.ok(this.locationServices.getLocationNotOcupated());
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<?> locationList( ) {
		return ResponseEntity.ok(this.locationServices.findAll());
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> addLocation(@RequestBody Location location ) {
		return ResponseEntity.ok(this.locationServices.save(location));
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> deleteLocation(@RequestBody Location location ) {
		return ResponseEntity.ok(this.locationServices.delete(location));
	}
	
	@RequestMapping(value = "/findLocationByIdAndSubstations", method = RequestMethod.GET)
	public ResponseEntity<?> findSubstationByIdAndLines(@RequestParam List<Long>ids, @RequestParam boolean showSubstations) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.locationServices.findLocationByIdAndSubstations(ids, showSubstations));				
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public ResponseEntity<?> edit(@RequestBody Location model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.locationServices.edit(model));						
	}
	
	@RequestMapping(value = "/deleteSubstations", method = RequestMethod.POST)
	public ResponseEntity<?> substationDelete(@RequestBody Location model) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.locationServices.deleteSubstation(model));						
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getReport() throws DocumentException, MalformedURLException, IOException {       
    	return this.locationServices.getReport();
    }

}
