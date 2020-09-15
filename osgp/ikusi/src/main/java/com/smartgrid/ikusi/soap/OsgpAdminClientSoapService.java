package com.smartgrid.ikusi.soap;

import org.opensmartgridplatform.adapter.ws.schema.admin.devicemanagement.UpdateKeyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.smartgrid.ikusi.model.Device;

/**
 * Service class used for sending and receiving SOAP messages to/from the
 * platform.
 */

public class OsgpAdminClientSoapService {

    private static final String PUBLIC_KEY =
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEFhUImXFJdqmputquVAc2CPdnn9Ju00M3m/Ice7wABNN+oAYKQbw/OceqvZmFF1+r4nO/vCm/f1JO5nEorE2jNQ==";
    private static final int OSLP_ELSTER_PROTOCOL = 4;

    @Autowired
    SoapRequestHelper soapRequestHelper;
    

    /**
     * Create a new UpdateKey Request adds it to the WebServiceTemplate and
     * sends it to the platform.
     */
    public void updateKeyRequest(final Device device) {

        final UpdateKeyRequest keyRequest = new UpdateKeyRequest();

        keyRequest.setDeviceIdentification(device.getDeviceIdentification());
        keyRequest.setPublicKey(PUBLIC_KEY);
        keyRequest.setProtocolInfoId(OSLP_ELSTER_PROTOCOL);

        final WebServiceTemplate template = this.soapRequestHelper.createAdminRequest();

        template.marshalSendAndReceive(keyRequest);
    }

}
