package com.smartgrid.ikusi.services;

import java.util.HashMap;
import java.util.Random;

import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Event;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.FindEventsRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.FindEventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgrid.ikusi.soap.SoapRequestHelper;

@Service
public class EventServices {
	@Autowired
	private SoapRequestHelper soapRequestHelper;
	
	@Autowired
	private SeverityServices severityServices;
	
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    public HashMap<String, Integer> getDataEvents() {
    	Random rand = new Random(); //instance of random class
        int upperbound = 1000;
    	HashMap<String, Integer> data = new HashMap<String, Integer>();
    	data.put("critics", rand.nextInt(upperbound));
    	data.put("mean", rand.nextInt(upperbound));
    	data.put("informative", rand.nextInt(upperbound));
    	return data; 
    }
    
    public void testSocket(HashMap<?,?> mapa) throws JsonProcessingException {
    	//JSONObject obj = new JSONObject(this.deviceFindEvents("CHI-2"));
    	this.eventSendMessage(mapa);
    }
    
    public void eventSendMessage(Object obj ) throws JsonProcessingException{
    	String topic = "/topic/event/receive";
    	ObjectMapper objectMapper = new ObjectMapper();
    	String json = objectMapper.writeValueAsString(obj);
        this.simpMessagingTemplate.convertAndSend(topic, json);
    }
    
    public void executeEventByDevice(String deviceIdentification) throws JsonProcessingException {
    	HashMap<String,Object> events = this.deviceFindEvents(deviceIdentification);    	 
    	 this.eventSendMessage(events);
    }
    
	public HashMap<String,Object> deviceFindEvents(String deviceIdentification) {
		System.out.println("Updated status event of device: " + deviceIdentification);
		HashMap<String,Object> events = new HashMap<String,Object>();
		try {
			FindEventsRequest request = new FindEventsRequest();
			request.setDeviceIdentification(deviceIdentification);
			final WebServiceTemplate template = this.soapRequestHelper.createCoreDeviceManagementRequest();
			FindEventsResponse response = (FindEventsResponse)template.marshalSendAndReceive(request);
			if(response!=null) {
				if(response.getEvents()!=null && response.getEvents().size() > 0) {				
					Event event = response.getEvents().get(0);
					events.put("deviceIdentification", deviceIdentification);
					events.put("event", event);
					events.put("severity", this.severityServices.findByCode(event.getDescription()));
					
				}
			}
			
		}catch (Exception e) {
			System.out.println(e.toString());
		}		
		return events;
	}

}
