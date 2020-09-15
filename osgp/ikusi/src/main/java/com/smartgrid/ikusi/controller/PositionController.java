package com.smartgrid.ikusi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartgrid.ikusi.services.PositionServices;

@RestController
@RequestMapping(value = "/position", produces = { "application/json" })
public class PositionController {
	
	@Autowired
	private PositionServices positionServices;

	@RequestMapping(value = "/findPosition", method = RequestMethod.GET)
	public ResponseEntity<?> findPosition(@RequestParam String deviceIdentification ) {				
		return ResponseEntity.status(HttpStatus.OK)
							 .body(this.positionServices.getPositions(deviceIdentification));
	}

}
