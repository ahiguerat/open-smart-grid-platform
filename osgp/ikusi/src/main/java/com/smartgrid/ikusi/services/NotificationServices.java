package com.smartgrid.ikusi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationServices {
	
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    /**
     * Process the information of other users and send data for others users connected in the endpoint
     * @param data Object that the user to se
     * @throws JsonProcessingException 
     */
    public void notificationWSSendMessage( Object record ) throws JsonProcessingException{
    	String topic = "/topic/firmware/receive";
    	//String topic = "/topic/firmware/receive/"+data.get("userId");
    	ObjectMapper objectMapper = new ObjectMapper();
    	String json = objectMapper.writeValueAsString(record);
        this.simpMessagingTemplate.convertAndSend(topic, json);
    }
    
}
