package com.smartgrid.ikusi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartgrid.ikusi.services.NotificationServices;

@RestController
public class NotificationController {
	
	@Autowired
	private NotificationServices notificationServices;
	
	
	//INSTALACION FIRMWARE
	 /** 
     * The clients the connect in this endpoint. Where will they are 
     * listening when other client send a message
     * @param userId : is the id the client  
     * */
    //@SubscribeMapping("/topic/firmware/receive/{company}")
    @SubscribeMapping("/topic/firmware/receive")
    public void companyWSReceiveMessage(/*@DestinationVariable String company*/){
        
    }
    
    //Los clientes envian mensajes al server mediante este topico
    @MessageMapping("/firmware/send")
    public void companyWSSendMessage(@Payload Object  json) throws JsonProcessingException {    	
        this.notificationServices.notificationWSSendMessage(json);
    }
            

}
