package com.smartgrid.ikusi.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.model.Manufacturer;
import com.smartgrid.ikusi.services.ManufacturerServices;



@RestController
@RequestMapping(value = "/manufacturer", produces = { "application/json" })
public class ManufacturerController {
	
	@Autowired
	private ManufacturerServices manufacturerServices;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> deviceAdd(@RequestBody Manufacturer manufacturer) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(manufacturerServices.manufacturerAdd(manufacturer));						
	}
	
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ResponseEntity<?> deviceChange(@RequestBody Manufacturer manufacturer) {
		return ResponseEntity.ok(manufacturerServices.manufacturerChange(manufacturer));
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ResponseEntity<?> deviceRemove(@RequestBody String code) {		
		return ResponseEntity.status(HttpStatus.OK)
				 .body(manufacturerServices.manufacturerRemove(code));		
	}
	
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public ResponseEntity<?> deviceFindAll( ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(manufacturerServices.manufacturerFindAll());				
	}
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getReport() throws DocumentException, MalformedURLException, IOException {       
    	return this.manufacturerServices.getReportManufacturer();
    }
	
}
