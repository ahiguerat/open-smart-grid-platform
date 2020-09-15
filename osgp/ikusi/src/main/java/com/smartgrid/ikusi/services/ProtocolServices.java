package com.smartgrid.ikusi.services;

import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.GetProtocolInfosRequest;
import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.GetProtocolInfosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.smartgrid.ikusi.soap.SoapRequestHelper;

@Service
public class ProtocolServices {
	
	@Autowired
	private SoapRequestHelper soapRequestHelper;

	public GetProtocolInfosResponse protocolFind( ) {
		GetProtocolInfosRequest request =  new GetProtocolInfosRequest();
		WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();
		GetProtocolInfosResponse response = (GetProtocolInfosResponse)template.marshalSendAndReceive(request);
		return response;
		
	}
}
