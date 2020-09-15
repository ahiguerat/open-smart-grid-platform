package com.smartgrid.ikusi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartgrid.ikusi.services.DashboardServices;

@RestController
@RequestMapping(value = "/dashboard", produces = { "application/json" })
public class DashboardController {
	
	@Autowired
	private DashboardServices dashboardServices;

	@RequestMapping(value = "/devicesByOrganizations", method = RequestMethod.GET)
	public ResponseEntity<?> getDevicesByOrganizations( ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.dashboardServices.getDevicesByOrganizations());
	}
	
	@RequestMapping(value = "/positions", method = RequestMethod.GET)
	public ResponseEntity<?> getPositions( ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.dashboardServices.getPositions());
	}
	
}
