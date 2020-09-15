package com.smartgrid.ikusi.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartgrid.ikusi.services.EventServices;

@RestController
@RequestMapping(value = "/event", produces = { "application/json" })
public class EventController {
	
	@Autowired
	private EventServices eventServices;
	
    //Los clientes escuchan mediante este topico.
    @SubscribeMapping("/topic/event/receive")
    public void eventReceiveMessage(/*@DestinationVariable String company*/){
        
    }
    
    //Los clientes envian mensajes al server mediante este topico
    @MessageMapping("/event/send")
    public void eventSendMessage(@Payload String  json) {
      //  JSONObject obj = new JSONObject(json);
       // this.eventServices.eventSendMessage(obj);
    }
    
    @RequestMapping(value = "/getEvents", method = RequestMethod.GET)
	public ResponseEntity<?> getEvents( ) {
    	return ResponseEntity.status(HttpStatus.OK)
				 .body(this.eventServices.getDataEvents());
	}
    
    @RequestMapping(value = "/doTestSocket", method = RequestMethod.POST)
	public void findDeviceNotAssoaciateStation(@RequestBody HashMap<?,?> mapa ) throws JsonProcessingException {
    	this.eventServices.testSocket(mapa);
		//return null;
	}
    
    @RequestMapping(value = "/deviceFindEvents", method = RequestMethod.GET)
	public ResponseEntity<?> deviceFindEvents(@RequestParam String deviceIdentification ) {
    	return ResponseEntity.status(HttpStatus.OK)
				 .body(this.eventServices.deviceFindEvents(deviceIdentification));
	}

}
