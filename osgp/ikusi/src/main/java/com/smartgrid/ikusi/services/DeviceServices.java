package com.smartgrid.ikusi.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

//import org.assertj.core.util.Arrays;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.ActivateDeviceAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.ActivateDeviceAsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.ActivateDeviceRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.ActivateDeviceResponse;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.GetProtocolInfosResponse;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.ProtocolInfo;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.RemoveDeviceRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.RemoveDeviceResponse;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.RevokeKeyRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.RevokeKeyResponse;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.UpdateDeviceProtocolRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.UpdateDeviceProtocolResponse;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.UpdateKeyRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.UpdateKeyResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.common.AsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.common.AsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.common.OsgpResultType;

//import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.RemoveDeviceRequest;
//import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.RemoveDeviceResponse;

import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.AddDeviceRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.AddDeviceResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.GetStatusAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.GetStatusAsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.GetStatusRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.GetStatusResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.UpdateDeviceRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.UpdateDeviceResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceFilter;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceLifecycleStatus;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.FindDevicesRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.FindDevicesResponse;
//import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.UpdateFirmwareAsyncRequest;
//import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.UpdateFirmwareResponse;
//import org.opensmartgridplatform.shared.infra.jms.RequestMessage;
//import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.FindAllDevicesRequest;
//import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.FindAllDevicesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
//import com.smartgrid.ikusi.enums.DeviceFirmwareStatusInstallation;
import com.smartgrid.ikusi.model.ActivateDevice;
import com.smartgrid.ikusi.model.DeviceStation;
import com.smartgrid.ikusi.model.FilterLocation;
import com.smartgrid.ikusi.reports.DeviceReport;
//import com.smartgrid.ikusi.model.Device;
//import com.smartgrid.ikusi.soap.OsgpAdminClientSoapService;
//import com.smartgrid.ikusi.soap.OsgpPublicLightingClientSoapService;
import com.smartgrid.ikusi.soap.SoapRequestHelper;

@Service
public class DeviceServices {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServices.class);

	@Autowired
	private ProtocolServices protocolServices;

	@Autowired
	private SoapRequestHelper soapRequestHelper;

	@Autowired
	private DeviceStationServices deviceStationServices;

	@Autowired
	private DeviceReport deviceReport;

//	@Autowired
//	private JmsTemplate ormSenderTemplate;

	@Autowired
	EventServices eventServices;

	public AddDeviceResponse deviceAdd(Device device) {
		AddDeviceRequest deviceRequest = new AddDeviceRequest();
		deviceRequest.setDevice(device);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreDeviceInstallationRequest();
		AddDeviceResponse reponse = (AddDeviceResponse) template.marshalSendAndReceive(deviceRequest);
		return reponse;
	}

	public UpdateDeviceResponse deviceUpdate(Device device) {
		UpdateDeviceRequest updateDeviceRequest = new UpdateDeviceRequest();
		updateDeviceRequest.setDeviceIdentification(device.getDeviceIdentification());
		updateDeviceRequest.setUpdatedDevice(device);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreDeviceInstallationRequest();
		UpdateDeviceResponse reponse = (UpdateDeviceResponse) template.marshalSendAndReceive(updateDeviceRequest);
		return reponse;
	}

	public RemoveDeviceResponse deviceRemove(String codeDevice) {
		RemoveDeviceRequest removeDeviceRequest = new RemoveDeviceRequest();
		removeDeviceRequest.setDeviceIdentification(codeDevice);
		final WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();
		RemoveDeviceResponse response = (RemoveDeviceResponse) template.marshalSendAndReceive(removeDeviceRequest);
		this.deviceStationServices.deleteByDeviceIdentification(codeDevice);
		
//		if(response!= null) {
//			response
//		}
		return response;
	}

	public FindDevicesResponse deviceFind(DeviceFilter filter) {
		FindDevicesRequest request = new FindDevicesRequest();
		// filter.setDeviceIdentification("DUR_3");
		request.setDeviceFilter(filter);
		request.setUsePages(true);

		// filter = new DeviceFilter();
		// request.setDeviceFilter(filter);
		//request.setPage(1);
		request.setPageSize(20);
		// request.setUsePages(false);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreDeviceManagementRequest();
		FindDevicesResponse response = (FindDevicesResponse) template.marshalSendAndReceive(request);

		int size = response.getPage().getPageSize() * response.getPage().getTotalPages();
		request.setUsePages(true);
		request.setPageSize(size + 1);
		response = (FindDevicesResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public UpdateDeviceProtocolResponse updateDeviceProtocol(UpdateDeviceProtocolRequest request) {
		final WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();
		UpdateDeviceProtocolResponse response = (UpdateDeviceProtocolResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public UpdateKeyResponse updateKey(UpdateKeyRequest request) {
		GetProtocolInfosResponse t = protocolServices.protocolFind();
		if (t != null) {

			for (int i = 0; i < t.getProtocolInfos().size(); i++) {
				if (t.getProtocolInfos().get(i).getId() == request.getProtocolInfoId()) {
					UpdateDeviceProtocolRequest reg = new UpdateDeviceProtocolRequest();
					reg.setDeviceIdentification(request.getDeviceIdentification());
					ProtocolInfo info = new ProtocolInfo();
					info.setId(request.getProtocolInfoId());
					info.setProtocol(t.getProtocolInfos().get(i).getProtocol());
					info.setProtocolVersion(t.getProtocolInfos().get(i).getProtocolVersion());
					reg.setProtocolInfo(info);
					this.updateDeviceProtocol(reg);

				}

			}
		}

		final WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();
		UpdateKeyResponse response = (UpdateKeyResponse) template.marshalSendAndReceive(request);
		return response;

	}

	public RevokeKeyResponse revokeKey(RevokeKeyRequest request) {
		WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();
		RevokeKeyResponse response = (RevokeKeyResponse) template.marshalSendAndReceive(request);
		return response;
	}

	// para activar los devices
	public ActivateDeviceAsyncResponse activateDevice(ActivateDevice activateDevice) {

		ActivateDeviceRequest request = new ActivateDeviceRequest();
		request.setDeviceIdentification(activateDevice.getDeviceIdentification());
		request.setIp(activateDevice.getIp());

		final WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();
		ActivateDeviceAsyncResponse response = (ActivateDeviceAsyncResponse) template.marshalSendAndReceive(request);

		return response;
	}

	public ActivateDeviceResponse getActivateDeviceResponse(ActivateDeviceAsyncRequest request) {

		System.out.println(request);
		
		final WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();
		ActivateDeviceResponse response = (ActivateDeviceResponse) template.marshalSendAndReceive(request);

		return response;
	}

	public List<org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device> findDeviceNotAssoaciateStation() {
		List<org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device> devices = this
				.deviceFind(new DeviceFilter()).getDevices();
		List<DeviceStation> devicesVinculated = this.deviceStationServices.getList();
		List<org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device> devicesNotUsed = devices.stream()
				.filter(r -> !devicesVinculated.stream()
						.filter(s -> s.getDeviceIdentification().equals(r.getDeviceIdentification())).findFirst()
						.isPresent())
				.collect(Collectors.toList());
		return devicesNotUsed;
	}

	public byte[] getReportDevice() throws DocumentException, IOException {
		FindDevicesResponse response = this.deviceFind(new DeviceFilter());
		return this.deviceReport.getFile(response.getDevices(), "DEVICE REPORTS");
	}

	public void getEvents() {
		int index = 0;
		
		final WebServiceTemplate template = this.soapRequestHelper.createCoreDeviceInstallationRequest();
		
		for (String deviceIdentification : this.deviceFind(new DeviceFilter()).getDevices().stream()
				.filter(d -> d.getDeviceLifecycleStatus().equals(DeviceLifecycleStatus.IN_USE))
				.map(device -> device.getDeviceIdentification()).collect(Collectors.toList())) {			
			this.getStatusAsyncResponse(deviceIdentification, template, index++);
		}
	}
	
	private void getStatusAsyncResponse(String deviceIdentification, WebServiceTemplate template, int index) {
		
		int timeInterval = 20000;
		Date begin = new Date(new Date().getTime() + index * timeInterval);
		
		System.out.println("Ge events of device: " + deviceIdentification + " at " + begin);

		DeviceServices ds = this;
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("Executing get events of device: " + deviceIdentification);
				GetStatusRequest getStatusRequest = new GetStatusRequest();
				getStatusRequest.setDeviceIdentification(deviceIdentification);
				
				GetStatusAsyncResponse response = (GetStatusAsyncResponse) template.marshalSendAndReceive(getStatusRequest);
				ds.getGetStatusResponse(response.getAsyncResponse(), template);
				timer.cancel();
				
			}
		}, begin, timeInterval);
	}

	private void getGetStatusResponse(AsyncResponse asyncResponse, WebServiceTemplate template) {
		
		AsyncRequest asyncRequest = new AsyncRequest();
		asyncRequest.setCorrelationUid(asyncResponse.getCorrelationUid());
		asyncRequest.setDeviceId(asyncResponse.getDeviceId());

		GetStatusAsyncRequest getStatusAsyncRequest = new GetStatusAsyncRequest();
		getStatusAsyncRequest.setAsyncRequest(asyncRequest);

		DeviceServices ds = this;

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			int counter = 0;

			@Override
			public void run() {
				try {
					GetStatusResponse wfResponse = (GetStatusResponse) template.marshalSendAndReceive(getStatusAsyncRequest);
					OsgpResultType result = wfResponse.getResult();

					if (!result.equals(OsgpResultType.NOT_FOUND)) {
						if (result.equals(OsgpResultType.OK)) {
							try {
								ds.eventServices.executeEventByDevice(asyncResponse.getDeviceId());
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						timer.cancel();
					}										
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					if (counter > 5) {
						timer.cancel();
						System.out.println(" DeviceServices Finished " + counter + ", Device:" + asyncRequest.getDeviceId());
					}
					counter++;
				}				
			}
		}, 0, 5000);

	}
	
	public Object devicesFilters(FilterLocation filter) {
		
		List<DeviceStation> listDevSta =  this.deviceStationServices.getListByFilters(filter);
		DeviceFilter filterDev = new DeviceFilter();
		FindDevicesResponse responseDevices = this.deviceFind(filterDev);
		if(responseDevices != null && responseDevices.getDevices().size() > 0 && listDevSta != null ) {
			return responseDevices.getDevices().stream().filter(
					r -> (
							listDevSta.stream()
							.filter(d-> d.getDeviceIdentification().equals(r.getDeviceIdentification()))
							.count())>0
					)
					.collect(Collectors.toList());					
		}
		return null;		
	}

}
