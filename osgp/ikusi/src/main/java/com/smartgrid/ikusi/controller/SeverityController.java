package com.smartgrid.ikusi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartgrid.ikusi.model.Severity;
import com.smartgrid.ikusi.services.SeverityServices;

@RestController
@RequestMapping(value = "/severity", produces = { "application/json" })
public class SeverityController {

	@Autowired
	private SeverityServices severityServices;
	
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	public ResponseEntity<?> findById( @RequestParam long idSeverity ) {
		return ResponseEntity.ok(this.severityServices.findById(idSeverity));
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> list( ) {
		return ResponseEntity.ok(this.severityServices.getList());
	}	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> stationAdd(@RequestBody Severity severity) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.severityServices.save(severity));						
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> lineDelete(@RequestBody Severity severity) {
		return ResponseEntity.status(HttpStatus.OK)
				 .body(this.severityServices.delete(severity.getIdSeverity()));						
	}
	
	@RequestMapping(value = "/findByCode", method = RequestMethod.GET)
	public ResponseEntity<?> findByCode( @RequestParam String code) {
		return ResponseEntity.ok(this.severityServices.findByCode(code));
	}
}
