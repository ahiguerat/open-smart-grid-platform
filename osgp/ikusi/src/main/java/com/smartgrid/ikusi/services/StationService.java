package com.smartgrid.ikusi.services;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceFilter;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.FindDevicesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.model.DeviceStation;
import com.smartgrid.ikusi.model.Station;
import com.smartgrid.ikusi.reports.StationsReport;
import com.smartgrid.ikusi.repository.StationRepository;

@Service
public class StationService {
	
	@Autowired
	private EventServices eventServices;

	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private DeviceServices deviceServices;
	
	@Autowired
	private StationsReport stationsReport;

	public List<Station> list() {
		List<Station> l = this.stationRepository.findByOrderByCodeAsc();
		return l;
	}
	
	public List<Station> listComlete() {
		List<Station> l = this.stationRepository.findByOrderByCodeAsc();		
		for (Station station : ListUtils.emptyIfNull(l)) {
			Iterator<DeviceStation> devices = station.getDevices().iterator();
			while (devices.hasNext()) {
				DeviceStation model = devices.next();					
				DeviceFilter df = new DeviceFilter();
				df.setDeviceIdentification(model.getDeviceIdentification());
				FindDevicesResponse response = this.deviceServices.deviceFind(df);
				if(response.getDevices() != null && response.getDevices().size() > 0) {
					model.setDevice(response.getDevices().get(0));
				}
			}			
		}
		return l;
	}

	public Station save(Station model) {
		this.stationRepository.save(model);
		return model;
	}

	public Station delete(Station model) {
		this.stationRepository.deleteById(model.getIdStation());
		return model;
	}

	public List<Station> getStationNotAssociated() {
		return this.stationRepository.getStationNotAssociated();
	}
	
	public List<Station> findStationByIdAndDevices(List<Long> ids, boolean showDevices) {
		List<Station> l = this.stationRepository.findAllById(ids);
		if (showDevices) {
			for (Station station : ListUtils.emptyIfNull(l)) {
				Iterator<DeviceStation> devices = station.getDevices().iterator();
				while (devices.hasNext()) {
					DeviceStation model = devices.next();					
					DeviceFilter df = new DeviceFilter();
					df.setDeviceIdentification(model.getDeviceIdentification());
					FindDevicesResponse response = this.deviceServices.deviceFind(df);
					if(response.getDevices() != null && response.getDevices().size() > 0) {
						model.setDevice(response.getDevices().get(0));
					}
					model.setEvent(this.eventServices.deviceFindEvents(model.getDeviceIdentification()));
				}				
			}			
		}		
		return l;
	}
	public byte[] getReport() throws DocumentException, IOException {
		List<Station> stations = this.listComlete();
		return this.stationsReport.getFile(stations, "STATIONS REPORTS");
	}
}
