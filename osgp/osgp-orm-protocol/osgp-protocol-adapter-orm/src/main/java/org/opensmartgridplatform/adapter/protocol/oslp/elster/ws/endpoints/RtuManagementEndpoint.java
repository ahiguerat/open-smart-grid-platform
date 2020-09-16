package org.opensmartgridplatform.adapter.protocol.oslp.elster.ws.endpoints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.opensmartgridplatform.adapter.protocol.oslp.elster.application.services.DeviceManagementService;
import org.opensmartgridplatform.adapter.ws.schema.orm.updatesoftfinished.UpdateSoftFinished;
import org.opensmartgridplatform.adapter.ws.schema.orm.updatesoftfinished.UpdateSoftFinishedResponse;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint(value = "ormRtuManagementEndpoint")
public class RtuManagementEndpoint {
	
	private static final String RTU_MANAGEMENT_NAMESPACE = "http://rtu.ws.es.ormazabal.www/";
	
	private static final Pattern pattern = Pattern.compile("seqNum=\"([0-9]+)\"");
	
	@Autowired
	private DeviceManagementService deviceManagementService;
	
	@PayloadRoot(localPart = "UpdateSoftFinished", namespace = RTU_MANAGEMENT_NAMESPACE)
    @ResponsePayload
    public JAXBElement<UpdateSoftFinishedResponse> updateSoftFinished(@RequestPayload JAXBElement<UpdateSoftFinished> request) throws OsgpException {
		System.out.println("UpdateSoftFinishedResponse");
		
		String xmlInput = request.getValue().getXmlwsinput().getXmlinput();
		
		System.out.println(xmlInput);
		
		Matcher matcher = pattern.matcher(xmlInput);
		
		if (matcher.find()) {
			int seqNum = Integer.valueOf(matcher.group(1));
			System.out.println("SeqNum: " + seqNum);
			this.deviceManagementService.sendUpdateFirmwareResponse(seqNum);
		}
		
		return new JAXBElement<UpdateSoftFinishedResponse>(new QName("http://rtu.ws.es.ormazabal.www/", "UpdateSoftFinished"),
				UpdateSoftFinishedResponse.class, new UpdateSoftFinishedResponse());
		
	}
	
}
