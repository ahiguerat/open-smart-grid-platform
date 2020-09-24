package org.opensmartgridplatform.adapter.protocol.oslp.elster.domain.valueobjects;

import org.opensmartgridplatform.adapter.protocol.oslp.elster.infra.messaging.DeviceResponseMessageSender;
import org.opensmartgridplatform.domain.core.entities.DeviceFirmwareFile;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;

public class UpdateFirmwareData {
	
	private DeviceFirmwareFile deviceFirmwareFile;
	private MessageMetadata messageMetadata;
	private DeviceResponseMessageSender deviceResponseMessageSender;
	
	public UpdateFirmwareData(DeviceFirmwareFile deviceFirmwareFile, MessageMetadata messageMetadata,
			DeviceResponseMessageSender deviceResponseMessageSender) {
		super();
		this.deviceFirmwareFile = deviceFirmwareFile;
		this.messageMetadata = messageMetadata;
		this.deviceResponseMessageSender = deviceResponseMessageSender;
	}

	public DeviceFirmwareFile getDeviceFirmwareFile() {
		return deviceFirmwareFile;
	}

	public MessageMetadata getMessageMetadata() {
		return messageMetadata;
	}

	public DeviceResponseMessageSender getDeviceResponseMessageSender() {
		return deviceResponseMessageSender;
	}
	
}