package com.smartgrid.ikusi.services;

import java.io.IOException;

import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.AddManufacturerRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.AddManufacturerResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.ChangeManufacturerRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.ChangeManufacturerResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllManufacturersRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.FindAllManufacturersResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.RemoveManufacturerRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.RemoveManufacturerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.model.Manufacturer;
import com.smartgrid.ikusi.reports.ManufacturerReport;
import com.smartgrid.ikusi.soap.SoapRequestHelper;

@Service
public class ManufacturerServices {

	@Autowired
	private SoapRequestHelper soapRequestHelper;
	
	@Autowired
	private ManufacturerReport manufacturerReport;

	public AddManufacturerResponse manufacturerAdd(Manufacturer auxManufacturer) {
		org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Manufacturer model = new org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Manufacturer();
		model.setCode(auxManufacturer.getCode());
		model.setId(auxManufacturer.getId());
		model.setName(auxManufacturer.getName());
		model.setUsePrefix(auxManufacturer.getUsePrefix());

		AddManufacturerRequest addManufacturerRequest = new AddManufacturerRequest();
		addManufacturerRequest.setManufacturer(model);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();		
		AddManufacturerResponse addManufacturerResponse = (AddManufacturerResponse) template.marshalSendAndReceive(addManufacturerRequest);
		return addManufacturerResponse;
	}

	public ChangeManufacturerResponse manufacturerChange(Manufacturer auxManufacturer) {
		org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Manufacturer model = new org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Manufacturer();
		model.setCode(auxManufacturer.getCode());
		model.setId(auxManufacturer.getId());
		model.setName(auxManufacturer.getName());
		model.setUsePrefix(auxManufacturer.getUsePrefix());

		ChangeManufacturerRequest changeManufacturerRequest = new ChangeManufacturerRequest();
		changeManufacturerRequest.setManufacturer(model);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		ChangeManufacturerResponse changeManufacturerResponse =  (ChangeManufacturerResponse)template.marshalSendAndReceive(changeManufacturerRequest);
		return changeManufacturerResponse;
	}

	public RemoveManufacturerResponse manufacturerRemove(String code) {		
		RemoveManufacturerRequest removeManufacturerRequest = new RemoveManufacturerRequest();		
		removeManufacturerRequest.setManufacturerId(code);
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		RemoveManufacturerResponse removeManufacturerResponse = (RemoveManufacturerResponse)template.marshalSendAndReceive(removeManufacturerRequest);
		return removeManufacturerResponse;
	}

	public FindAllManufacturersResponse manufacturerFindAll() {
		FindAllManufacturersRequest findAllManufacturersRequest = new FindAllManufacturersRequest();
		final WebServiceTemplate template = this.soapRequestHelper.createCoreRequest();
		final FindAllManufacturersResponse response = (FindAllManufacturersResponse) template
				.marshalSendAndReceive(findAllManufacturersRequest);

		return response;				
	}
	
	public byte[] getReportManufacturer() throws DocumentException, IOException {
		FindAllManufacturersResponse response = this.manufacturerFindAll();
		return manufacturerReport.getFile(response.getManufacturers(), "MANUFACTURER REPORTS");		
	}

}
