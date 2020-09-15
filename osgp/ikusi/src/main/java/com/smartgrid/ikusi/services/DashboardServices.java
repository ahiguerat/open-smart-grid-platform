package com.smartgrid.ikusi.services;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceFilter;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.FindDevicesResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.DeviceModel;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Manufacturer;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllDeviceModelsResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllManufacturersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgrid.ikusi.model.Location;

@Service
public class DashboardServices {

	@Autowired
	private DeviceServices deviceServices;
	
	@Autowired
	private ManufacturerServices manufacturerServices;
	
	@Autowired
	private DeviceModelServices deviceModelServices;
	
	@Autowired 
	private LocationServices locationServices;
	
	private Integer getTotalDevices(String organisation, String manufacturer, String model) {
		Integer total = 0;
		try {
			DeviceFilter filter = new DeviceFilter();
			filter.setManufacturer(manufacturer);
			filter.setModel(model);
			filter.setOrganisationIdentification(organisation);
			FindDevicesResponse response = this.deviceServices.deviceFind(filter);
			total = response!=null && response.getDevices() != null ? response.getDevices().size(): 0; 
		}catch(Exception e) {
			System.out.println(e.toString());			
		}
		return total;
		
	}
	
	public HashMap<String, HashMap<String, Integer>> getDevicesByOrganizations() {
		String organisations = "test-org";
		HashMap<String, HashMap<String, Integer>> mapa =  new HashMap<String, HashMap<String, Integer>>(); 
		FindAllManufacturersResponse manufacturerResponse = this.manufacturerServices.manufacturerFindAll();
		FindAllDeviceModelsResponse deviceModelResponse = this.deviceModelServices.deviceModelFindAll();
		
		if(manufacturerResponse != null) {
			
			for(Manufacturer man : ListUtils.emptyIfNull(manufacturerResponse.getManufacturers())) {
				mapa.put(man.getCode(), null);				
			}
			
			if(deviceModelResponse != null) {
				for(DeviceModel model : ListUtils.emptyIfNull(deviceModelResponse.getDeviceModels())) {
					Integer deviceTotal = this.getTotalDevices(organisations, model.getManufacturer(), model.getModelCode());
					HashMap<String, Integer> mapModel = mapa.get(model.getManufacturer());
					if(mapModel == null) {
						mapModel = new HashMap<String, Integer>();
						mapa.replace(model.getManufacturer(), mapModel);
					}					
					mapModel.put(model.getModelCode(), deviceTotal);
					
				}
			}						
		}
		return mapa;				
	}
	
	
	public List<Location> getPositions() {
		return this.locationServices.findAllComplete();		
	}
	
	
}
