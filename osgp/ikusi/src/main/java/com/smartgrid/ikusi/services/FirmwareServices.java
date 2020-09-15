package com.smartgrid.ikusi.services;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.opensmartgridplatform.adapter.ws.schema.core.common.AsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.common.AsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.common.OsgpResultType;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.AddFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.AddFirmwareResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.ChangeFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.ChangeFirmwareResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.DeviceFirmware;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllFirmwaresRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllFirmwaresResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindFirmwareResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Firmware;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.GetDeviceFirmwareHistoryRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.GetDeviceFirmwareHistoryResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.RemoveFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.RemoveFirmwareResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.SaveCurrentDeviceFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.SaveCurrentDeviceFirmwareResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.SwitchFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.SwitchFirmwareResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.UpdateFirmwareAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.UpdateFirmwareAsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.UpdateFirmwareRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.UpdateFirmwareResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.smartgrid.ikusi.enums.DeviceFirmwareStatusInstallation;
import com.smartgrid.ikusi.model.DeviceFirmwareStatus;
import com.smartgrid.ikusi.soap.SoapRequestHelper;

@Service
public class FirmwareServices {

	@Autowired
	private DeviceFirmwareStatusServices deviceFirmwareStatusServices;

	@Autowired
	private SoapRequestHelper soapRequestHelper;

	public AddFirmwareResponse addFile(Firmware firmware) {
		FileServices fileServices = new FileServices();
		fileServices.readCompressFile(firmware);

		AddFirmwareRequest request = new AddFirmwareRequest();
		request.setFirmware(firmware);
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		AddFirmwareResponse response = (AddFirmwareResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public ChangeFirmwareResponse changeFile(ChangeFirmwareRequest request) {

		FileServices fileServices = new FileServices();
		fileServices.readCompressFile(request.getFirmware());

		if (request.getFirmware().getFile() == null) {
			FindFirmwareResponse model = this.find(request.getFirmware());
			if (model != null && model.getFirmware() != null) {
				request.getFirmware().setFirmwareModuleData(model.getFirmware().getFirmwareModuleData());
			}
		}
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		ChangeFirmwareResponse response = (ChangeFirmwareResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public AddFirmwareResponse add(Firmware firmware) {
		AddFirmwareRequest request = new AddFirmwareRequest();
		request.setFirmware(firmware);
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		AddFirmwareResponse response = (AddFirmwareResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public ChangeFirmwareResponse change(Firmware firmware) {
		ChangeFirmwareRequest request = new ChangeFirmwareRequest();
		request.setFirmware(firmware);
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		ChangeFirmwareResponse response = (ChangeFirmwareResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public RemoveFirmwareResponse remove(Firmware firmware) {
		RemoveFirmwareRequest request = new RemoveFirmwareRequest();
		request.setId(firmware.getId());
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		RemoveFirmwareResponse response = (RemoveFirmwareResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public FindAllFirmwaresResponse findAll(Firmware firmware) {
		FindAllFirmwaresRequest request = new FindAllFirmwaresRequest();
		if (firmware.getManufacturer() != null)
			request.setManufacturer(firmware.getManufacturer());
		if (firmware.getModelCode() != null)
			request.setModelCode(firmware.getModelCode());
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		FindAllFirmwaresResponse response = (FindAllFirmwaresResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public FindFirmwareResponse find(Firmware firmware) {
		FindFirmwareRequest request = new FindFirmwareRequest();
		request.setFirmwareId(firmware.getId());
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		FindFirmwareResponse response = (FindFirmwareResponse) template.marshalSendAndReceive(request);
		return response;
	}

	public UpdateFirmwareAsyncResponse updateFirmware(HashMap<String, String> model) {
		// falta validar para que un dispositivo solo pueda hacer una actualizacion
		DeviceFirmwareStatus status = this.deviceFirmwareStatusServices
				.findLastStatusFirmwareInstalledDevice(model.get("deviceIdentification"));
		if (status != null && status.getStatus().equals(DeviceFirmwareStatusInstallation.UPDATING.toString())) {
			throw new java.lang.RuntimeException(
					"The device is installing a firmware version, please wait a few minutes..");
		}

		UpdateFirmwareRequest request = new UpdateFirmwareRequest();
		request.setDeviceIdentification(model.get("deviceIdentification"));
		request.setFirmwareIdentification(model.get("firmwareIdentification"));
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		UpdateFirmwareAsyncResponse response = (UpdateFirmwareAsyncResponse) template.marshalSendAndReceive(request);

		DeviceFirmwareStatus install = new DeviceFirmwareStatus();
		install.setFirmware(model.get("firmwareIdentification"));
		install.setDeviceIdentification(model.get("deviceIdentification"));
		install.setMessage("Installing Firmware");
		install.setNetworkAddress(model.get("networkAddress"));
		deviceFirmwareStatusServices.saveDeviceFirmwareStatusAndSendNotification(install);
		
		this.getUpdateFirmwareResponse(response.getAsyncResponse(), template);

		return response;
	}

	public SwitchFirmwareResponse switchFirmware(SwitchFirmwareRequest request) {
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		SwitchFirmwareResponse response = (SwitchFirmwareResponse) template.marshalSendAndReceive(request);
		return response;

	}

	public SaveCurrentDeviceFirmwareResponse saveCurrentDeviceFirmware(DeviceFirmware deviceFirmware)
			throws DatatypeConfigurationException {
		Date date = new Date();
		GregorianCalendar gregoriancalendar = new GregorianCalendar();
		gregoriancalendar.setTime(date);
		XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregoriancalendar);

		if (deviceFirmware.getFirmware().getFile() != null) {
			FileServices fileServices = new FileServices();
			fileServices.readCompressFile(deviceFirmware.getFirmware());

		}
		SaveCurrentDeviceFirmwareRequest request = new SaveCurrentDeviceFirmwareRequest();
		deviceFirmware.setInstallationDate(calendar);
		request.setDeviceFirmware(deviceFirmware);

		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		SaveCurrentDeviceFirmwareResponse response = (SaveCurrentDeviceFirmwareResponse) template
				.marshalSendAndReceive(request);
		return response;
	}

	public GetDeviceFirmwareHistoryResponse deviceFirmwareHistory(String deviceIdentification) {
		GetDeviceFirmwareHistoryRequest request = new GetDeviceFirmwareHistoryRequest();
		request.setDeviceIdentification(deviceIdentification);
		WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		GetDeviceFirmwareHistoryResponse response = (GetDeviceFirmwareHistoryResponse) template
				.marshalSendAndReceive(request);
		return response;

	}
	
	private void getUpdateFirmwareResponse(AsyncResponse asyncResponse, WebServiceTemplate template) {
		AsyncRequest asyncRequest = new AsyncRequest();
		asyncRequest.setCorrelationUid(asyncResponse.getCorrelationUid());
		asyncRequest.setDeviceId(asyncResponse.getDeviceId());

		UpdateFirmwareAsyncRequest updateFirmwareAsyncRequest = new UpdateFirmwareAsyncRequest();
		updateFirmwareAsyncRequest.setAsyncRequest(asyncRequest);

		FirmwareServices fs = this;

		Date begin = new Date(new Date().getTime() + 60000);
		int timeInterval = 60000;
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			int counter = 0;

			@Override
			public void run() {
				
				try {
					UpdateFirmwareResponse wfResponse = (UpdateFirmwareResponse) template.marshalSendAndReceive(updateFirmwareAsyncRequest);			
					OsgpResultType result = wfResponse.getResult();					
					System.out.println("Try: " + counter + ", Device:" + asyncRequest.getDeviceId() + ", result: "+ result);

					if (!result.equals(OsgpResultType.NOT_FOUND)) {
						try {
							fs.deviceFirmwareStatusServices.finishUpdateFirmware(asyncRequest.getDeviceId(),
									result.equals(OsgpResultType.OK) ? DeviceFirmwareStatusInstallation.OK.toString()
											: DeviceFirmwareStatusInstallation.NOT_OK.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						timer.cancel();						
					}
					
				}catch(Exception e) {
					e.printStackTrace();

				} finally {
					if (counter > 14) {
						try {
							fs.deviceFirmwareStatusServices.finishUpdateFirmware(asyncRequest.getDeviceId(),
									DeviceFirmwareStatusInstallation.NOT_OK.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						timer.cancel();
					}
					counter++;
				}
			}
			
		}, begin, timeInterval);

	}
	
	public Firmware getDeviceFirmwareInstalled(String deviceIdentification, String manufacturer, String model) {
		Firmware firmware = new Firmware();
		firmware.setManufacturer(manufacturer);
		firmware.setModelCode(model);
		DeviceFirmwareStatus device = this.deviceFirmwareStatusServices.findLastFirmwareInstalledByDeviceAndStatus(deviceIdentification, DeviceFirmwareStatusInstallation.OK.toString());
		if(device ==null)
			return null;
		FindAllFirmwaresResponse firmwares = this.findAll(firmware);
		Optional<Firmware> firmInstalled = firmwares.getFirmwares().stream().filter(r-> r.getFilename().equals(device.getFirmware())).findFirst();
		return firmInstalled.isPresent() ? firmInstalled.get() : null;
		
	}

}
