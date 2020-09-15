package com.smartgrid.ikusi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartgrid.ikusi.enums.DeviceFirmwareStatusInstallation;
import com.smartgrid.ikusi.model.DeviceFirmwareStatus;
import com.smartgrid.ikusi.repository.DeviceFirmwareStatusRepository;

@Service
public class DeviceFirmwareStatusServices {
	
	@Autowired
	private NotificationServices notificationServices;
	
	@Autowired
	private DeviceFirmwareStatusRepository deviceFirmwareStatusRepository;
	
	public DeviceFirmwareStatus finishUpdateFirmware(String deviceId, String status) throws JsonProcessingException {
		
		System.out.println("Finish device firmware status, device: " + deviceId + ", status: "+ status);
		
		DeviceFirmwareStatus deviceFirmwareStatus = this.findLastStatusFirmwareInstalledDevice(deviceId);
		deviceFirmwareStatus.setStatus(status);
		
		return this.update(deviceFirmwareStatus);
	}
	
	public DeviceFirmwareStatus saveDeviceFirmwareStatusAndSendNotification(DeviceFirmwareStatus model) {
		DeviceFirmwareStatus record =  null;
		try {
			record = this.save(model);			
			this.notificationServices.notificationWSSendMessage(record);
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		return record;
	}
	
	public DeviceFirmwareStatus save(DeviceFirmwareStatus model) {
		model.setStatus(DeviceFirmwareStatusInstallation.UPDATING.toString());
		DeviceFirmwareStatus deviceFirmwareStatus =  this.deviceFirmwareStatusRepository
				.findOneByStatusAndDeviceIdentificationOrderByIdDeviceFirmwareStatus( DeviceFirmwareStatusInstallation.UPDATING.toString(), model.getDeviceIdentification());
		if (deviceFirmwareStatus == null)
			return this.deviceFirmwareStatusRepository.save(model);
		throw new java.lang.RuntimeException("The device has status in updating, for the moment not is possible add new installations.");
	}
	
	public DeviceFirmwareStatus update(DeviceFirmwareStatus model) throws JsonProcessingException {
		DeviceFirmwareStatus deviceFirmwareStatus =  this.deviceFirmwareStatusRepository
				.findOneByStatusAndDeviceIdentificationOrderByIdDeviceFirmwareStatus( DeviceFirmwareStatusInstallation.UPDATING.toString(), model.getDeviceIdentification());
		if(deviceFirmwareStatus != null) {
			deviceFirmwareStatus.setStatus(model.getStatus());
			deviceFirmwareStatus.setMessage(model.getMessage());
			DeviceFirmwareStatus record = this.deviceFirmwareStatusRepository.save(deviceFirmwareStatus);
			this.notificationServices.notificationWSSendMessage(record);
			return record;
		}
		throw new java.lang.RuntimeException("Not is possible update the state device, because its has the status different to 'updating'.");
	}
	
	public DeviceFirmwareStatus findById(long id) {
		Optional<DeviceFirmwareStatus> deviceFirmwareStatus =  this.deviceFirmwareStatusRepository.findById(id);
		if(deviceFirmwareStatus.isPresent())
			return deviceFirmwareStatus.get();
		return null;
	}
	
	public DeviceFirmwareStatus findLastStatusFirmwareInstalledDevice(String deviceIdentification) {	
		return this.deviceFirmwareStatusRepository.findLastStatusFirmwareInstalledDevice(deviceIdentification);
	}
	

	public List<DeviceFirmwareStatus> findByDeviceIdentification(String deviceIdentification) {
		return this.deviceFirmwareStatusRepository.findAllByDeviceIdentificationOrderByIdDeviceFirmwareStatusDesc(deviceIdentification);
	}
	
	public List<DeviceFirmwareStatus> findLastStatusFirmwareInstalledToDevices() {
		return this.deviceFirmwareStatusRepository.findLastStatusFirmwareInstalledToDevices();
	}
	
	public DeviceFirmwareStatus findLastFirmwareInstalledByDeviceAndStatus(String deviceIdentification, String status) {	
		return this.deviceFirmwareStatusRepository.findLastFirmwareInstalledByDeviceAndStatus(deviceIdentification, status);
	}

}
