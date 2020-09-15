package com.smartgrid.ikusi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartgrid.ikusi.model.DeviceFirmwareStatus;

@Repository
public interface DeviceFirmwareStatusRepository extends JpaRepository<DeviceFirmwareStatus, Long> {
	String stateByDeviceStatus = "with cte as ( " + 
			"	select max(id_device_firmware_status) as id " + 
			"	from device_firmware_status " +
			"   where status = ?2 " +
			"	group by device_identification " + 
			")" + 
			"select d.* " + 
			"from cte " + 
			"join device_firmware_status d on d.id_device_firmware_status = cte.id " + 
			"where d.device_identification = ?1 ";
	
	String stateByDevice = "with cte as ( " + 
			"	select max(id_device_firmware_status) as id " + 
			"	from device_firmware_status " + 
			"	group by device_identification " + 
			")" + 
			"select d.* " + 
			"from cte " + 
			"join device_firmware_status d on d.id_device_firmware_status = cte.id " + 
			"where d.device_identification = ?1 ";
	
	String state = "with cte as ( " + 
			"	select max(id_device_firmware_status) as id " + 
			"	from device_firmware_status " + 
			"	group by device_identification " + 
			")" + 
			"select d.* " + 
			"from cte " + 
			"join device_firmware_status d on d.id_device_firmware_status = cte.id " + 
			"order by id_device_firmware_status desc ";
	
	
	DeviceFirmwareStatus findOneByStatusAndDeviceIdentificationOrderByIdDeviceFirmwareStatus(String status, String deviceIdentification);
	
	List<DeviceFirmwareStatus> findAllByDeviceIdentificationOrderByIdDeviceFirmwareStatusDesc(String deviceIdentification);
	

	@Query(value = state, nativeQuery = true)
	List<DeviceFirmwareStatus> findLastStatusFirmwareInstalledToDevices( );
	
	@Query(value = stateByDevice, nativeQuery = true)
	DeviceFirmwareStatus findLastStatusFirmwareInstalledDevice(String deviceIdentification);
	
	@Query(value = stateByDeviceStatus, nativeQuery = true)
	DeviceFirmwareStatus findLastFirmwareInstalledByDeviceAndStatus(String deviceIdentification, String status);	

}
