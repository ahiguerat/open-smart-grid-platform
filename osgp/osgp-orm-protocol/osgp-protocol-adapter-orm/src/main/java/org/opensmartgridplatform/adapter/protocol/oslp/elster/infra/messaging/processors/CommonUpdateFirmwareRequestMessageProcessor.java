/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.processors;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.opensmartgridplatform.adapter.protocol.oslp.elster.application.services.DeviceManagementService;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.DeviceRequest;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.DeviceResponse;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.DeviceResponseHandler;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.FirmwareLocation;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.requests.UpdateFirmwareDeviceRequest;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.DeviceRequestMessageProcessor;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.OslpEnvelopeProcessor;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.networking.DeviceService;
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto;
import org.opensmartgridplatform.dto.valueobjects.FirmwareUpdateMessageDataContainer;
import org.opensmartgridplatform.oslp.OslpEnvelope;
import org.opensmartgridplatform.oslp.SignedOslpEnvelopeDto;
import org.opensmartgridplatform.oslp.UnsignedOslpEnvelopeDto;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for processing common update firmware request messages
 */
@Component("oslpCommonUpdateFirmwareRequestMessageProcessor")
public class CommonUpdateFirmwareRequestMessageProcessor extends DeviceRequestMessageProcessor {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUpdateFirmwareRequestMessageProcessor.class);

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private DeviceManagementService deviceManagementService;

    @Autowired
    private FirmwareLocation firmwareLocation;

    public CommonUpdateFirmwareRequestMessageProcessor() {
        super(MessageType.UPDATE_FIRMWARE);
    }

    // IDEA: the FirmwareLocation class in domain and dto can/must be deleted!
    // Or, this
    // setup has to be changed in order to reuse the FirmwareLocation class in
    // the domain!!

    @Override
    public void processMessage(final ObjectMessage message) {
        LOGGER.debug("Processing common update firmware request message");

        MessageMetadata messageMetadata;
        FirmwareUpdateMessageDataContainer firmwareUpdateMessageDataContainer;
        try {
            messageMetadata = MessageMetadata.fromMessage(message);
            firmwareUpdateMessageDataContainer = (FirmwareUpdateMessageDataContainer) message.getObject();
        } catch (final JMSException e) {
            LOGGER.error("UNRECOVERABLE ERROR, unable to read ObjectMessage instance, giving up.", e);
            return;
        }

        try {
//            final String firmwareIdentification = firmwareUpdateMessageDataContainer.getFirmwareUrl();

            this.printDomainInfo(messageMetadata.getMessageType(), messageMetadata.getDomain(),
                    messageMetadata.getDomainVersion());
            
            this.deviceManagementService.updateSoft(messageMetadata, this.responseMessageSender, firmwareUpdateMessageDataContainer);
            
        } catch (final RuntimeException e) {
            this.handleError(e, messageMetadata);
        }
    }
}
