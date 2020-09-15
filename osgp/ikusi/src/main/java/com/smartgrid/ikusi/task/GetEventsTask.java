package com.smartgrid.ikusi.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smartgrid.ikusi.services.DeviceServices;

@Component
public class GetEventsTask {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
	private DeviceServices deviceservices;

	@Scheduled(fixedRate = 300000)
	public void reportCurrentTime() {
		System.out.println("Exeute getEvens at: " + dateFormat.format(new Date()));
		this.deviceservices.getEvents();
		
	}
}
