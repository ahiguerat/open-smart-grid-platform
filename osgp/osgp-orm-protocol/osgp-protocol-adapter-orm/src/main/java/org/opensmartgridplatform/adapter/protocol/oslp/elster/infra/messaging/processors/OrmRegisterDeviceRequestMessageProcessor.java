/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.processors;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo.Station;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.application.services.DeviceRegistrationService;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.application.services.OrmDeviceService;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.DeviceMessageStatus;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.DeviceRequest;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.DeviceResponse;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.DeviceResponseHandler;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.requests.ResumeScheduleDeviceRequest;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.requests.SetLightDeviceRequest;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.responses.EmptyDeviceResponse;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.domain.entities.Position;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.domain.entities.RtuDevice;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.domain.repositories.RtuDeviceRepository;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.DeviceRequestMessageProcessor;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.OslpEnvelopeProcessor;
import org.opensmartgridplatform.adapter.ws.schema.orm.getstationinfo.GetStationInfo;
import org.opensmartgridplatform.adapter.ws.schema.orm.getstationinfo.GetStationInfoResponse;
import org.opensmartgridplatform.adapter.ws.schema.orm.getstationinfo.XmlwsInput;
import org.opensmartgridplatform.adapter.ws.shared.db.domain.repositories.writable.WritableDeviceRepository;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceLifecycleStatus;
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto;
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto;
import org.opensmartgridplatform.dto.valueobjects.ResumeScheduleMessageDataContainerDto;
import org.opensmartgridplatform.oslp.Oslp;
import org.opensmartgridplatform.oslp.OslpEnvelope;
import org.opensmartgridplatform.oslp.SignedOslpEnvelopeDto;
import org.opensmartgridplatform.oslp.UnsignedOslpEnvelopeDto;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

/**
 * Class for processing public lighting set light request messages
 */
@Component("ormRegisterDeviceRequestMessageProcessor")
public class OrmRegisterDeviceRequestMessageProcessor extends DeviceRequestMessageProcessor
		implements OslpEnvelopeProcessor {

	@Autowired
	private DeviceRegistrationService deviceRegistrationService;

	@Autowired
	@Qualifier("getStationWSTemplate")
	private WebServiceTemplate webServiceTemplate;
	
	@Autowired
	private RtuDeviceRepository rtuDeviceRepository;
	
	@Autowired
	private OrmDeviceService ormDeviceService;

	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OrmRegisterDeviceRequestMessageProcessor.class);

	public OrmRegisterDeviceRequestMessageProcessor() {
		super(MessageType.REGISTER_DEVICE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(final ObjectMessage message) {
		LOGGER.debug("Processing orm register device request message");

		MessageMetadata messageMetadata;
		DeviceRegistrationDataDto registerDeviceMessage;
		try {
			messageMetadata = MessageMetadata.fromMessage(message);
			registerDeviceMessage = (DeviceRegistrationDataDto) message.getObject();
		} catch (final JMSException e) {
			LOGGER.error("UNRECOVERABLE ERROR, unable to read ObjectMessage instance, giving up.", e);
			return;
		}

		try {
			this.printDomainInfo(messageMetadata.getMessageType(), messageMetadata.getDomain(),
					messageMetadata.getDomainVersion());

			final String deviceIdentification = messageMetadata.getDeviceIdentification();
			final String deviceType = registerDeviceMessage.getDeviceType().toString();
			final boolean hasSchedule = registerDeviceMessage.isHasSchedule();
			
			String ipAddress = registerDeviceMessage.getIpAddress().trim();
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			
			this.ormDeviceService.updateDeviceLifecycleStatus(deviceIdentification, DeviceLifecycleStatus.UNDER_TEST);
			
			String commonInput = "<CommonInput reqId=\"M01\" seqNum=\"7\"\n"
					+ " xmlns=\"http://com.ormazabal.es/requestRTU/CommonInput\"\n"
					+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
					+ " xsi:schemaLocation=\"http://com.ormazabal.es/requestRTU/CommonInput CommonInput.xsd \" />";

			XmlwsInput xmlwsInput = new XmlwsInput();
			xmlwsInput.setCommoninput(commonInput);

			GetStationInfo getStationInfo = new GetStationInfo();
			getStationInfo.setXmlwsinput(xmlwsInput);

			JAXBElement<GetStationInfoResponse> response = (JAXBElement<GetStationInfoResponse>) webServiceTemplate
					.marshalSendAndReceive("http://"+ ipAddress +":8084/wsrtu/GetStationInfo",
							new JAXBElement<GetStationInfo>(
									new QName("http://rtu.ws.es.ormazabal.www/", "GetStationInfo"),
									GetStationInfo.class, getStationInfo));

			String xmlOutput = response.getValue().getReturn().getXmloutput();			
						
			// Parse xml output web service response
			JAXBContext jaxbContext = JAXBContext.newInstance(Station.class);

			XMLInputFactory xif = XMLInputFactory.newFactory();
			xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
			StreamSource source = new StreamSource(new StringReader((xmlOutput)));
			XMLStreamReader xsr = xif.createXMLStreamReader(source);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Station station = (Station) jaxbUnmarshaller.unmarshal(xsr);

			System.out.println(station);
			
			RtuDevice rtuDevice = this.rtuDeviceRepository.findByDeviceIdentification(deviceIdentification);
			if (rtuDevice == null) {
				rtuDevice = new RtuDevice(deviceIdentification);
			}

			List<Position> positions = new ArrayList<Position>();
			for (org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo.Position p: station.getRtuInfo().getPositions().getPosition()) {
				positions.add(new Position(
						p.getNumPos(),
						p.getType() != null ? p.getType().getValue() : null,
						p.getName() != null ? p.getName().getValue() : null,
						p.getCnx() != null ? p.getCnx().getStatus() : null,
						rtuDevice));
			}
			
			rtuDevice.setPositions(positions);
			
			this.rtuDeviceRepository.save(rtuDevice);
			
			// Send message to OSGP-CORE to save IP Address, device type and has
			// schedule values in OSGP-CORE database.
			this.deviceRegistrationService.sendDeviceRegisterRequest(inetAddress, deviceType, hasSchedule,
					deviceIdentification);

		} catch (final RuntimeException e) {
			this.handleError(e, messageMetadata);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void processSignedOslpEnvelope(final String deviceIdentification,
			final SignedOslpEnvelopeDto signedOslpEnvelopeDto) {

		// final UnsignedOslpEnvelopeDto unsignedOslpEnvelopeDto =
		// signedOslpEnvelopeDto.getUnsignedOslpEnvelopeDto();
		// final OslpEnvelope oslpEnvelope = signedOslpEnvelopeDto.getOslpEnvelope();
		// final String correlationUid = unsignedOslpEnvelopeDto.getCorrelationUid();
		// final String organisationIdentification =
		// unsignedOslpEnvelopeDto.getOrganisationIdentification();
		// final String domain = unsignedOslpEnvelopeDto.getDomain();
		// final String domainVersion = unsignedOslpEnvelopeDto.getDomainVersion();
		// final String messageType = unsignedOslpEnvelopeDto.getMessageType();
		// final int messagePriority = unsignedOslpEnvelopeDto.getMessagePriority();
		// final String ipAddress = unsignedOslpEnvelopeDto.getIpAddress();
		// final int retryCount = unsignedOslpEnvelopeDto.getRetryCount();
		// final boolean isScheduled = unsignedOslpEnvelopeDto.isScheduled();
		//
		// final DeviceResponseHandler setLightDeviceResponseHandler = new
		// DeviceResponseHandler() {
		//
		// @Override
		// public void handleResponse(final DeviceResponse deviceResponse) {
		// if (((EmptyDeviceResponse)
		// deviceResponse).getStatus().equals(DeviceMessageStatus.OK)) {
		// // If the response is OK, just log it. The resumeSchedule()
		// // function will be called next.
		// LOGGER.info("setLight() successful for device : {}",
		// deviceResponse.getDeviceIdentification());
		// } else {
		// // If the response is not OK, send a response message to the
		// // responses queue.
		// OrmRegisterDeviceRequestMessageProcessor.this.handleEmptyDeviceResponse(deviceResponse,
		// OrmRegisterDeviceRequestMessageProcessor.this.responseMessageSender, domain,
		// domainVersion, messageType, retryCount);
		// }
		// }
		//
		// @Override
		// public void handleException(final Throwable t, final DeviceResponse
		// deviceResponse) {
		// OrmRegisterDeviceRequestMessageProcessor.this.handleUnableToConnectDeviceResponse(deviceResponse,
		// t, domain, domainVersion, messageType, isScheduled, retryCount);
		// }
		// };
		//
		// final DeviceRequest setLightdeviceRequest = DeviceRequest.newBuilder()
		// .organisationIdentification(organisationIdentification).deviceIdentification(deviceIdentification)
		// .correlationUid(correlationUid).domain(domain).domainVersion(domainVersion).messageType(messageType)
		// .messagePriority(messagePriority).ipAddress(ipAddress).retryCount(retryCount).isScheduled(isScheduled)
		// .build();
		//
		// // Execute a ResumeSchedule call with 'immediate = false' and 'index
		// // = 0' as arguments.`
		// final ResumeScheduleMessageDataContainerDto
		// resumeScheduleMessageDataContainer = new
		// ResumeScheduleMessageDataContainerDto(
		// 0, false);
		//
		// final DeviceResponseHandler resumeScheduleDeviceResponseHandler =
		// this.publicLightingResumeScheduleRequestMessageProcessor
		// .createResumeScheduleDeviceResponseHandler(domain, domainVersion,
		// MessageType.RESUME_SCHEDULE.name(),
		// retryCount, isScheduled);
		//
		// final ResumeScheduleDeviceRequest resumeScheduleDeviceRequest = new
		// ResumeScheduleDeviceRequest(DeviceRequest
		// .newBuilder().organisationIdentification(organisationIdentification)
		// .deviceIdentification(deviceIdentification).correlationUid(correlationUid).domain(domain)
		// .domainVersion(domainVersion).messageType(MessageType.RESUME_SCHEDULE.name())
		// .messagePriority(messagePriority).ipAddress(ipAddress).retryCount(retryCount).isScheduled(isScheduled),
		// resumeScheduleMessageDataContainer);
		//
		// try {
		// this.deviceService.doSetLight(oslpEnvelope, setLightdeviceRequest,
		// resumeScheduleDeviceRequest,
		// setLightDeviceResponseHandler, resumeScheduleDeviceResponseHandler,
		// ipAddress);
		// } catch (final IOException e) {
		// this.handleError(e, correlationUid, organisationIdentification,
		// deviceIdentification, domain, domainVersion,
		// messageType, messagePriority, retryCount);
		// }
	}
}
