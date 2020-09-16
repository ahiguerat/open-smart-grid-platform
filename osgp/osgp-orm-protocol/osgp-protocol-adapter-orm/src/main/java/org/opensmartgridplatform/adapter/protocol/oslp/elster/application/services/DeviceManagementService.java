/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.oslp.elster.application.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.opensmartgridplatform.adapter.protocol.orm.schema.getevents.EventList;
import org.opensmartgridplatform.adapter.protocol.orm.schema.getevents.Evento;
import org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo.Position;
import org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo.Station;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.application.services.oslp.OslpDeviceSettingsService;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.domain.entities.OslpDevice;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.domain.valueobjects.UpdateFirmwareData;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.exceptions.ProtocolAdapterException;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.DeviceResponseMessageSender;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.OsgpRequestMessageSender;
import org.opensmartgridplatform.adapter.ws.schema.orm.getevents.GetEvents;
import org.opensmartgridplatform.adapter.ws.schema.orm.getevents.GetEventsResponse;
import org.opensmartgridplatform.adapter.ws.schema.orm.getstationinfo.GetStationInfo;
import org.opensmartgridplatform.adapter.ws.schema.orm.getstationinfo.GetStationInfoResponse;
import org.opensmartgridplatform.adapter.ws.schema.orm.getstationinfo.XmlwsInput;
import org.opensmartgridplatform.adapter.ws.schema.orm.updatesoft.UpdateSoft;
import org.opensmartgridplatform.adapter.ws.schema.orm.updatesoft.UpdateSoftResponse;
import org.opensmartgridplatform.adapter.ws.shared.db.domain.repositories.writable.WritableDeviceRepository;
import org.opensmartgridplatform.adapter.ws.shared.db.domain.repositories.writable.WritableFirmwareFileRepository;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.DeviceFirmwareFile;
import org.opensmartgridplatform.domain.core.entities.DeviceModel;
import org.opensmartgridplatform.domain.core.entities.FirmwareFile;
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto;
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto;
import org.opensmartgridplatform.dto.valueobjects.EventTypeDto;
import org.opensmartgridplatform.dto.valueobjects.FirmwareUpdateMessageDataContainer;
import org.opensmartgridplatform.dto.valueobjects.orm.DeviceEventDto;
import org.opensmartgridplatform.dto.valueobjects.orm.PositionDto;
import org.opensmartgridplatform.dto.valueobjects.orm.RtuDeviceDto;
import org.opensmartgridplatform.oslp.Oslp;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.exceptionhandling.TechnicalException;
import org.opensmartgridplatform.shared.infra.jms.DeviceMessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage;
import org.opensmartgridplatform.shared.infra.jms.RequestMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

@Service(value = "oslpDeviceManagementService")
@Transactional(value = "transactionManager")
public class DeviceManagementService {

	private static final AtomicInteger sequence = new AtomicInteger(1);

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceManagementService.class);

	private static final ConcurrentHashMap<Integer, UpdateFirmwareData> mapper = new ConcurrentHashMap<Integer, UpdateFirmwareData>();

	@Autowired
	private OslpDeviceSettingsService oslpDeviceSettingsService;

	@Autowired
	private OsgpRequestMessageSender osgpRequestMessageSender;

	@Autowired
	@Qualifier("getStationWSTemplate")
	private WebServiceTemplate getStationWSTemplate;

	@Autowired
	@Qualifier("updateSoftWSTemplate")
	private WebServiceTemplate updateSoftWSTemplate;

	@Autowired
	@Qualifier("getEventsWSTemplate")
	private WebServiceTemplate getEventsWSTemplate;

	@Autowired
	private WritableDeviceRepository deviceRepository;

	@Autowired
	private WritableFirmwareFileRepository firmwareFileRepository;

	@Resource
	@Qualifier("ftpUrl")
	private String ftpUrl;

	@Resource
	@Qualifier("ftpPort")
	private String ftpPort;

	@Resource
	@Qualifier("ftpLogin")
	private String ftpLogin;

	@Resource
	@Qualifier("ftpPassword")
	private String ftpPassword;

	@Resource
	@Qualifier("ftpPath")
	private String ftpPath;

	@Resource
	@Qualifier("timeInterval")
	private int timeInterval;

	/**
	 * Constructor
	 */
	public DeviceManagementService() {
		// Parameterless constructor required for transactions...
	}

	// === ADD EVENT NOTIFICATION ===

	/**
	 * Create a new event notification DTO with the given arguments.
	 *
	 * @param deviceIdentification
	 *            The identification of the device.
	 * @param deviceUid
	 *            The UID of the device.
	 * @param eventType
	 *            The event type. May not be empty or null.
	 * @param description
	 *            The description which came along with the event from the device.
	 *            May be an empty string, but not null.
	 * @param index
	 *            The index of the relay. May not be null.
	 * @param timestamp
	 *            The date and time of the event. May be an empty string or null.
	 */
	private EventNotificationDto createEventNotificationDto(final String deviceIdentification, final String deviceUid,
			final String eventType, final String description, final Integer index, final String timestamp) {
		// Assert.notNull(eventType);
		// Assert.notNull(description);
		// Assert.notNull(index);

		LOGGER.info("addEventNotification called for device: {} with eventType: {}, description: {} and timestamp: {}",
				deviceIdentification, eventType, description, timestamp);

		// Convert timestamp to DateTime.
		DateTime dateTime;
		if (StringUtils.isEmpty(timestamp)) {
			dateTime = DateTime.now();
			LOGGER.info("timestamp is empty, using DateTime.now(): {}", dateTime);
		} else {
			final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss Z");
			dateTime = dateTimeFormatter.withOffsetParsed().parseDateTime(timestamp.concat(" +0000"));
			LOGGER.info("parsed timestamp from string: {} to DateTime: {}", timestamp, dateTime);
		}

		return new EventNotificationDto(deviceUid, dateTime, EventTypeDto.valueOf(eventType), description, index);
	}

	/**
	 * Send a list of event notifications to OSGP Core.
	 *
	 * @param deviceUid
	 *            The identification of the device.
	 * @param eventNotifications
	 *            The event notifications.
	 */
	public void addEventNotifications(final String deviceUid, final List<Oslp.EventNotification> eventNotifications) {
		LOGGER.info("addEventNotifications called for device {}", deviceUid);
		final OslpDevice oslpDevice = this.oslpDeviceSettingsService.getDeviceByUid(deviceUid);
		final String deviceIdentification = oslpDevice.getDeviceIdentification();

		final List<EventNotificationDto> eventNotificationDtos = new ArrayList<>();
		for (final Oslp.EventNotification eventNotification : eventNotifications) {
			final String eventType = eventNotification.getEvent().name();
			final String description = eventNotification.getDescription();
			final int index = eventNotification.getIndex().isEmpty() ? 0 : (int) eventNotification.getIndex().byteAt(0);
			String timestamp = eventNotification.getTimestamp();
			LOGGER.debug("-->> timestamp: {}", timestamp);
			// Hack for faulty firmware version. RTC_NOT_SET event can contain
			// illegal timestamp value of 20000000xxxxxx.
			if (!StringUtils.isEmpty(timestamp) && timestamp.startsWith("20000000")) {
				final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
				timestamp = DateTime.now().withZone(DateTimeZone.UTC).toString(dateTimeFormatter);
				LOGGER.info("Using DateTime.now() instead of '20000000xxxxxx', value is: {}", timestamp);
			}
			final EventNotificationDto dto = this.createEventNotificationDto(deviceIdentification, deviceUid, eventType,
					description, index, timestamp);
			eventNotificationDtos.add(dto);
		}

		final RequestMessage requestMessage = new RequestMessage("no-correlationUid", "no-organisation",
				deviceIdentification, new ArrayList<>(eventNotificationDtos));

		this.osgpRequestMessageSender.send(requestMessage, MessageType.ADD_EVENT_NOTIFICATION.name());
	}

	// === UPDATE KEY ===

	public void updateKey(final MessageMetadata messageMetadata,
			final DeviceResponseMessageSender responseMessageSender, final String publicKey) {

		final String deviceIdentification = messageMetadata.getDeviceIdentification();
		final String organisationIdentification = messageMetadata.getOrganisationIdentification();
		LOGGER.info("updateKey called for device: {} for organisation: {} with new publicKey: {}", deviceIdentification,
				organisationIdentification, publicKey);

		try {
			OslpDevice oslpDevice = this.oslpDeviceSettingsService
					.getDeviceByDeviceIdentification(deviceIdentification);
			if (oslpDevice == null) {
				// Device not found, create new device
				LOGGER.debug("Device [{}] does not exist, creating new device", deviceIdentification);
				oslpDevice = new OslpDevice(deviceIdentification);
				oslpDevice = this.oslpDeviceSettingsService.addDevice(oslpDevice);
			}

			oslpDevice.updatePublicKey(publicKey);
			this.oslpDeviceSettingsService.updateDevice(oslpDevice);

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.OK, null, responseMessageSender);

		} catch (final Exception e) {
			LOGGER.error("Unexpected exception during updateKey", e);
			final TechnicalException ex = new TechnicalException(ComponentType.UNKNOWN,
					"Exception occurred while updating key", e);

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK, ex, responseMessageSender);
		}
	}

	// === REVOKE KEY ===

	public void revokeKey(final MessageMetadata messageMetadata,
			final DeviceResponseMessageSender responseMessageSender) {

		final String deviceIdentification = messageMetadata.getDeviceIdentification();
		final String organisationIdentification = messageMetadata.getOrganisationIdentification();
		LOGGER.info("revokeKey called for device: {} for organisation: {}", deviceIdentification,
				organisationIdentification);

		try {
			final OslpDevice oslpDevice = this.oslpDeviceSettingsService
					.getDeviceByDeviceIdentification(deviceIdentification);
			if (oslpDevice == null) {
				throw new ProtocolAdapterException(String.format("Device not found: %s", deviceIdentification));
			}

			oslpDevice.revokePublicKey();
			this.oslpDeviceSettingsService.updateDevice(oslpDevice);

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.OK, null, responseMessageSender);

		} catch (final Exception e) {
			LOGGER.error("Unexpected exception during revokeKey", e);
			final TechnicalException ex = new TechnicalException(ComponentType.UNKNOWN,
					"Exception occurred while revoking key", e);
			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK, ex, responseMessageSender);
		}
	}

	public void getStationInfo(final MessageMetadata messageMetadata,
			final DeviceResponseMessageSender responseMessageSender, DeviceRegistrationDataDto deviceRegistrationData) {
		LOGGER.info("Activate device called");

		final String deviceIdentification = messageMetadata.getDeviceIdentification();
		final String ipAddress = deviceRegistrationData.getIpAddress().trim();

		try {
			Station station = this.getStationInfoWs(ipAddress);
			System.out.println(station);

			List<PositionDto> positionsDto = new ArrayList<PositionDto>();
			RtuDeviceDto rtuDeviceDto = new RtuDeviceDto(deviceIdentification, positionsDto);
			rtuDeviceDto.setIp(ipAddress);

			for (Position position : station.getRtuInfo().getPositions().getPosition()) {
				positionsDto.add(new PositionDto(position.getNumPos(),
						position.getType() != null ? position.getType().getValue() : null,
						position.getName() != null ? position.getName().getValue() : null,
						position.getCnx() != null ? position.getCnx().getStatus() : null));
			}

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.OK, null, responseMessageSender,
					rtuDeviceDto);
		} catch (final Exception e) {
			LOGGER.error("Unexpected exception during device activation", e);
			final TechnicalException ex = new TechnicalException(ComponentType.UNKNOWN,
					"Exception occurred while activate device", e);

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK, ex, responseMessageSender);
		}
	}

	@SuppressWarnings("unchecked")
	public void getEvents(MessageMetadata messageMetadata, DeviceResponseMessageSender responseMessageSender) {

		final String deviceIdentification = messageMetadata.getDeviceIdentification();
		final Device device = deviceRepository.findByDeviceIdentification(deviceIdentification);
		final String ipAddress = device.getIpAddress().trim();

		String commonInput = this.getCommonInput("M04", 7);

		String xmlInput = "<EventDates fini=\"000000000000\" ffin=\"000000000000\" \n"
				+ "xmlns=\"http://com.ormazabal.es/requestRTU/EventDates\"\n"
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "xsi:schemaLocation=\"http://com.ormazabal.es/requestRTU/EventDates EventDates.xsd \" >\n"
				//+ "<event grupeve=\"0\" tipeve=\"0\"/>\n" + 
				+"</EventDates>";

		org.opensmartgridplatform.adapter.ws.schema.orm.getevents.XmlwsInput xmlwsInput = new org.opensmartgridplatform.adapter.ws.schema.orm.getevents.XmlwsInput();
		xmlwsInput.setCommoninput(commonInput);
		xmlwsInput.setXmlinput(xmlInput);

		GetEvents getEvents = new GetEvents();
		getEvents.setXmlwsinput(xmlwsInput);

		LOGGER.info("Call get events ws to ip: {}", ipAddress);
		JAXBElement<GetEventsResponse> response = (JAXBElement<GetEventsResponse>) getEventsWSTemplate
				.marshalSendAndReceive("http://" + ipAddress + ":8084/wsrtu/GetEvents", new JAXBElement<GetEvents>(
						new QName("http://rtu.ws.es.ormazabal.www/", "GetEvents"), GetEvents.class, getEvents));

		byte[] xmlOutput = response.getValue().getReturn().getXmloutput();

		Evento evento = null;
		DeviceEventDto lastEvent = null;

		try {
			GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(xmlOutput));
			InputStreamReader reader = new InputStreamReader(gZIPInputStream);
			BufferedReader in = new BufferedReader(reader);

			String readed = "", newLine;
			while ((newLine = in.readLine()) != null) {
				readed += newLine;
			}

			// Parse xml output web service response
			JAXBContext jaxbContext = JAXBContext.newInstance(EventList.class);

			XMLInputFactory xif = XMLInputFactory.newFactory();
			xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
			StreamSource source = new StreamSource(new StringReader((readed)));
			XMLStreamReader xsr = xif.createXMLStreamReader(source);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			EventList eventList = (EventList) jaxbUnmarshaller.unmarshal(xsr);
			int sizeEvents = eventList.getEventos().size();
			evento = sizeEvents > 0 ? eventList.getEventos().get(0) : null;

			if (evento != null) {
				lastEvent = new DeviceEventDto();
				lastEvent.setDeviceIdentification(messageMetadata.getDeviceIdentification());
				lastEvent.setDescription(evento.getCeve());
			}

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.OK, null, responseMessageSender,
					lastEvent);
		} catch (Exception e) {
			LOGGER.error("Unexpected exception during updte soft", e);
			final TechnicalException ex = new TechnicalException(ComponentType.UNKNOWN,
					"Exception occurred while activate device", e);

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK, ex, responseMessageSender);
		}
	}

	@SuppressWarnings("unchecked")
	public void updateSoft(final MessageMetadata messageMetadata,
			final DeviceResponseMessageSender responseMessageSender,
			FirmwareUpdateMessageDataContainer firmwareUpdateMessageDataContainer) {
		LOGGER.info("Update soft called");

		final String deviceIdentification = messageMetadata.getDeviceIdentification();
		final String firmwareUrl = firmwareUpdateMessageDataContainer.getFirmwareUrl();

		final Device device = deviceRepository.findByDeviceIdentification(deviceIdentification);
		final DeviceModel deviceModel = device.getDeviceModel();
		final String ipAddress = device.getIpAddress().trim();

		final FirmwareFile firmwareFile = firmwareFileRepository.findTopByFilenameOrderByIdDesc(firmwareUrl);
		final String filename = firmwareFile.getFilename();

		final String path = new StringBuilder(this.ftpPath).append(File.separator)
				.append(deviceModel.getManufacturer().getCode().replaceAll(" ", "_")).append(File.separator)
				.append(deviceModel.getModelCode().replaceAll(" ", "_")).toString();

		DeviceFirmwareFile deviceFirmwareFile = new DeviceFirmwareFile(device, firmwareFile, new Date(), null);

		final int seqNum = getSeqNum(
				new UpdateFirmwareData(deviceFirmwareFile, messageMetadata, responseMessageSender));

		LOGGER.info("SeqNum={}", seqNum);
		LOGGER.info("FTP data: login={}, password={}, url={}, path={}, name={}", this.ftpLogin, this.ftpPassword,
				this.ftpUrl, path, filename);
		String commonInput = this.getCommonInput("M05", seqNum);

		String xmlInput = "<UpdateSoft \n" + "xmlns=\"http://com.ormazabal.es/requestRTU/UpdateSoft\"\n"
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "xsi:schemaLocation=\"http://com.ormazabal.es/requestRTU/UpdateSoft UpdateSoft.xsd \" >\n" + "\n"
				+ "<ftpsrv login=\"" + this.ftpLogin + "\" pass=\"" + this.ftpPassword + "\" port=\"" + this.ftpPort
				+ "\" url=\"" + this.ftpUrl + "\"/>\n" + "<filesRTU path=\"" + path + "\">\n" + "<file name=\""
				+ filename + "\" order=\"1\"/>\n" + "</filesRTU>\n" + "</UpdateSoft>";
		LOGGER.info("xmlInput={}", xmlInput);
//		String xmlInput2 = "<UpdateSoft \n" + 
//				"				xmlns=\"http://com.ormazabal.es/requestRTU/UpdateSoft\"\n" + 
//				"				xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"               \n" + 
//				"				xsi:schemaLocation=\"http://com.ormazabal.es/requestRTU/UpdateSoft UpdateSoft.xsd \" >\n" + 
//				"				\n" + 
//				"				<ftpsrv login=\"rtu\" pass=\"ormazabalrtu\" port=\"21\" url=\"192.168.247.21\"/>\n" + 
//				"				<filesRTU path=\"/ftp/files\">\n" + 
//				"				   <file name=\"ekorrtuws.190125.v723a-wso-04fcv.bin-imx.tgz\" order=\"1\"/>\n" + 
//				"				</filesRTU>\n" + 
//				"			</UpdateSoft>";
//		LOGGER.info("xmlInput2={}", xmlInput2);
		org.opensmartgridplatform.adapter.ws.schema.orm.updatesoft.XmlwsInput xmlwsInput = new org.opensmartgridplatform.adapter.ws.schema.orm.updatesoft.XmlwsInput();
		xmlwsInput.setCommoninput(commonInput);
		xmlwsInput.setXmlinput(xmlInput);

		UpdateSoft updateSoft = new UpdateSoft();
		updateSoft.setXmlwsinput(xmlwsInput);

		try {

			Map<String, String> currentVersion = this.getVersion(this.getStationInfoWs(ipAddress));

			JAXBElement<UpdateSoftResponse> response = (JAXBElement<UpdateSoftResponse>) updateSoftWSTemplate
					.marshalSendAndReceive("http://" + ipAddress + ":8084/wsrtu/UpdateSoft",
							new JAXBElement<UpdateSoft>(new QName("http://rtu.ws.es.ormazabal.www/", "UpdateSoft"),
									UpdateSoft.class, updateSoft));

			LOGGER.info(response.getValue().getReturn().getCommonoutput());

			final DeviceManagementService dms = this;
			Timer timer = new Timer();

			Date begin = new Date(new Date().getTime() + this.timeInterval);

			timer.schedule(new TimerTask() {
				int counter = 0;
				
				public boolean validateVersions(Map<String, String> currentVersion, Map<String, String> updatedVersion) {
					int differents = 0;
					
					for(Map.Entry<String, String> v : updatedVersion.entrySet()) {
						if(currentVersion.containsKey(v.getKey())){
							LOGGER.info("update  code:{} value:{} -> current:{}", v.getKey(), v.getValue(), currentVersion.get(v.getKey()));
							if(!currentVersion.get(v.getKey()).equals(v.getValue())) {
								differents++;
							}
						}else {
							differents++;
						}
					}
					LOGGER.info("numberDifferents: {}", differents);
					
					return differents>0;
				}

				@Override
				public void run() {
					// call the method
					try {
						counter++;
						LOGGER.info("GetStationInfo pull counter: {}", counter);
						if (counter > 15) {
							dms.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK,
									new TechnicalException(ComponentType.UNKNOWN, "Timeout exceded",
											new Exception("Time out exceded")),
									responseMessageSender);
							timer.cancel();
						}

						LOGGER.info("Invocando get station info");
						Map<String, String> updatedVersion = dms.getVersion(dms.getStationInfoWs(ipAddress));

						LOGGER.info("Current version: {}, updatedVersion: {}", currentVersion.toString(), updatedVersion.toString());
						validateSeqNum(seqNum);

						if (!this.validateVersions(updatedVersion, currentVersion)) {
							dms.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK,
									new TechnicalException(ComponentType.UNKNOWN, "Update firmware failed",
											new Exception("Unexpected error")),
									responseMessageSender);
						} else {
							LOGGER.info("Changed detected: true");
							dms.sendResponseMessage(messageMetadata, ResponseMessageResultType.OK, null,
									responseMessageSender, deviceFirmwareFile);
						}
						timer.cancel();
					} catch (Exception e) {
						LOGGER.error("ERROR " + e.getMessage());
					}
				}
			}, begin, this.timeInterval);

		} catch (Exception e) {
			LOGGER.error("Unexpected exception during updte soft", e);
			final TechnicalException ex = new TechnicalException(ComponentType.UNKNOWN,
					"Exception occurred while activate device", new Exception("Update firmware request failed"));

			this.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK, ex, responseMessageSender);
		}
	}

	public void sendUpdateFirmwareResponse(int seqNum) {
		UpdateFirmwareData updateFirmwareData = validateSeqNum(seqNum);
		if (updateFirmwareData != null) {
			this.sendResponseMessage(updateFirmwareData.getMessageMetadata(), ResponseMessageResultType.OK, null,
					updateFirmwareData.getDeviceResponseMessageSender(), updateFirmwareData.getDeviceFirmwareFile());
		}

	}

	@SuppressWarnings("unchecked")
	private Station getStationInfoWs(String ipAddress) throws JAXBException, XMLStreamException {
		XmlwsInput xmlwsInput = new XmlwsInput();
		xmlwsInput.setCommoninput(this.getCommonInput("M01", 7));

		GetStationInfo getStationInfo = new GetStationInfo();
		getStationInfo.setXmlwsinput(xmlwsInput);

		JAXBElement<GetStationInfoResponse> response = (JAXBElement<GetStationInfoResponse>) getStationWSTemplate
				.marshalSendAndReceive("http://" + ipAddress + ":8084/wsrtu/GetStationInfo",
						new JAXBElement<GetStationInfo>(new QName("http://rtu.ws.es.ormazabal.www/", "GetStationInfo"),
								GetStationInfo.class, getStationInfo));

		String xmlOutput = response.getValue().getReturn().getXmloutput();

		// Parse xml output web service response
		JAXBContext jaxbContext = JAXBContext.newInstance(Station.class);

		XMLInputFactory xif = XMLInputFactory.newFactory();
		xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		StreamSource source = new StreamSource(new StringReader((xmlOutput)));
		XMLStreamReader xsr = xif.createXMLStreamReader(source);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (Station) jaxbUnmarshaller.unmarshal(xsr);

	}

	private Map<String, String> getVersion(Station station) {
		Map<String, String> products = new HashMap<String, String>();
		station.getRtuInfo().getProducts().getP().stream().forEach(p->{ 
			if(!products.containsKey(p.getCode())) {
				products.put(p.getCode(), p.getVersion());
			}
		});
//		return station.getRtuInfo().getProducts().getP().stream().filter(p -> p.getCode().equals(code)).findFirst()
//				.get().getVersion();
		return products;
	}

	private String getCommonInput(String reqId, int seqNum) {
		return "<CommonInput reqId=\"" + reqId + "\" seqNum=\"" + seqNum + "\"\n"
				+ " xmlns=\"http://com.ormazabal.es/requestRTU/CommonInput\"\n"
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ " xsi:schemaLocation=\"http://com.ormazabal.es/requestRTU/CommonInput CommonInput.xsd \" />";
	}

	private void sendResponseMessage(final MessageMetadata messageMetadata, final ResponseMessageResultType result,
			final OsgpException osgpException, final DeviceResponseMessageSender responseMessageSender) {

		final DeviceMessageMetadata deviceMessageMetadata = new DeviceMessageMetadata(messageMetadata);
		final ProtocolResponseMessage responseMessage = ProtocolResponseMessage.newBuilder()
				.domain(messageMetadata.getDomain()).domainVersion(messageMetadata.getDomainVersion())
				.deviceMessageMetadata(deviceMessageMetadata).result(result).osgpException(osgpException).build();

		responseMessageSender.send(responseMessage);
	}

	private void sendResponseMessage(final MessageMetadata messageMetadata, final ResponseMessageResultType result,
			final OsgpException osgpException, final DeviceResponseMessageSender responseMessageSender,
			final Serializable dataObject) {

		final DeviceMessageMetadata deviceMessageMetadata = new DeviceMessageMetadata(messageMetadata);
		final ProtocolResponseMessage responseMessage = ProtocolResponseMessage.newBuilder()
				.domain(messageMetadata.getDomain()).domainVersion(messageMetadata.getDomainVersion())
				.deviceMessageMetadata(deviceMessageMetadata).result(result).osgpException(osgpException)
				.dataObject(dataObject).build();

		responseMessageSender.send(responseMessage);
	}

	public static Integer getSeqNum(UpdateFirmwareData updateFirmwareAck) {
		int seqNum = sequence.getAndIncrement();
		mapper.put(seqNum, updateFirmwareAck);

		return seqNum;
	}

	public static UpdateFirmwareData validateSeqNum(int seqNum) {
		return mapper.remove(seqNum);
	}
}
