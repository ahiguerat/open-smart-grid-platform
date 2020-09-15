package com.smartgrid.ikusi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartgrid.ikusi.services.ProtocolServices;

@RestController
@RequestMapping(value = "/protocol", produces = { "application/json" })
public class ProtocolController {
	
	@Autowired
	private ProtocolServices protocolServices;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<?> deviceList( ) {
		return ResponseEntity.ok(protocolServices.protocolFind());
	}

}
