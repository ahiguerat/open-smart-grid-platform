package com.smartgrid.ikusi.services;

import java.io.IOException;

import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.AddDeviceModelRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.AddDeviceModelResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.ChangeDeviceModelRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.ChangeDeviceModelResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.DeviceModel;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllDeviceModelsRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllDeviceModelsResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.RemoveDeviceModelRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.RemoveDeviceModelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.reports.DeviceModelReport;
import com.smartgrid.ikusi.soap.SoapRequestHelper;

@Service
public class DeviceModelServices {
	
	@Autowired
	private SoapRequestHelper soapRequestHelper;
	
	@Autowired
	private DeviceModelReport deviceModelReport;
	
	public AddDeviceModelResponse deviceModelAdd(DeviceModel model) {
		AddDeviceModelRequest  request = new AddDeviceModelRequest();		
		request.setDeviceModel(model);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		AddDeviceModelResponse addDeviceModelResponse = (AddDeviceModelResponse)template.marshalSendAndReceive(request);
		return addDeviceModelResponse;				
	}
	
	public ChangeDeviceModelResponse deviceModelChange(DeviceModel model) {
		ChangeDeviceModelRequest request = new ChangeDeviceModelRequest();
		request.setDeviceModel(model);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		ChangeDeviceModelResponse changeDeviceModelResponse = (ChangeDeviceModelResponse)template.marshalSendAndReceive(request);
		return changeDeviceModelResponse;		
	}
	
	public RemoveDeviceModelResponse deviceModelRemove(DeviceModel model) {
		RemoveDeviceModelRequest request  = new RemoveDeviceModelRequest();
		request.setDeviceManufacturerId(model.getManufacturer());
		request.setDeviceModelId(model.getModelCode());
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		RemoveDeviceModelResponse response = (RemoveDeviceModelResponse)template.marshalSendAndReceive(request);
		return response;				
	}
	
	public FindAllDeviceModelsResponse deviceModelFindAll() {
		FindAllDeviceModelsRequest request = new FindAllDeviceModelsRequest();
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		FindAllDeviceModelsResponse response = (FindAllDeviceModelsResponse)template.marshalSendAndReceive(request);
		return response;	
	}
	
	public byte[] getReportDeviceModle() throws DocumentException, IOException {
		FindAllDeviceModelsResponse response = this.deviceModelFindAll();
		return deviceModelReport.getFile(response.getDeviceModels(), "DEVICE MODEL REPORTS");
		
	}
	
}
