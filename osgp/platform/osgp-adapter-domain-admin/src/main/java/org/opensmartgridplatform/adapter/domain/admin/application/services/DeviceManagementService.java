/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.admin.application.services;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.OrmRtuDevice;
import org.opensmartgridplatform.domain.core.entities.Position;
import org.opensmartgridplatform.domain.core.entities.RtuDevice;
import org.opensmartgridplatform.domain.core.entities.Ssld;
import org.opensmartgridplatform.domain.core.exceptions.PlatformException;
import org.opensmartgridplatform.domain.core.exceptions.UnknownEntityException;
import org.opensmartgridplatform.domain.core.repositories.DeviceRepository;
import org.opensmartgridplatform.domain.core.repositories.OrmRtuDeviceRepository;
import org.opensmartgridplatform.domain.core.repositories.RtuDeviceRepository;
import org.opensmartgridplatform.domain.core.repositories.SsldRepository;
import org.opensmartgridplatform.domain.core.validation.PublicKey;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceLifecycleStatus;
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto;
import org.opensmartgridplatform.dto.valueobjects.orm.PositionDto;
import org.opensmartgridplatform.dto.valueobjects.orm.RtuDeviceDto;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalExceptionType;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.exceptionhandling.TechnicalException;
import org.opensmartgridplatform.shared.infra.jms.RequestMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType;
import org.opensmartgridplatform.shared.validation.Identification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "domainAdminDeviceManagementService")
@Transactional(value = "transactionManager")
public class DeviceManagementService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceManagementService.class);

    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
	private OrmRtuDeviceRepository ormRtuDeviceRepository;

    @Autowired
    private SsldRepository ssldRepository;

    /**
     * Constructor
     */
    public DeviceManagementService() {
        // Parameterless constructor required for transactions...
    }

    // === UPDATE KEY ===

    public void updateKey(final String organisationIdentification, @Identification final String deviceIdentification,
            final String correlationUid, final String messageType, @PublicKey final String publicKey)
            throws FunctionalException {

        LOGGER.info("MessageType: {}. Updating key for device [{}] on behalf of organisation [{}]", messageType,
                deviceIdentification, organisationIdentification);

        try {
            this.organisationDomainService.searchOrganisation(organisationIdentification);
        } catch (final UnknownEntityException e) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.DOMAIN_ADMIN, e);
        }

        this.osgpCoreRequestMessageSender.send(
                new RequestMessage(correlationUid, organisationIdentification, deviceIdentification, publicKey),
                messageType, null);
    }

    
    public void activateDevice(final String organisationIdentification, @Identification final String deviceIdentification,
            final String correlationUid, final String messageType, final String ipAddress)
            throws FunctionalException {

        LOGGER.info("MessageType: {}. Activate device [{}] on behalf of organisation [{}]", messageType,
                deviceIdentification, organisationIdentification);

        try {
            this.organisationDomainService.searchOrganisation(organisationIdentification);
        } catch (final UnknownEntityException e) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.DOMAIN_ADMIN, e);
        }
        
        final DeviceRegistrationDataDto deviceRegistrationData = new DeviceRegistrationDataDto(ipAddress, "RTU", false);

        this.osgpCoreRequestMessageSender.send(
                new RequestMessage(correlationUid, organisationIdentification, deviceIdentification, deviceRegistrationData),
                messageType, null);
    }

    
    public void handleUpdateKeyResponse(final String deviceIdentification, final String organisationIdentification,
            final String correlationUid, final String messageType, final ResponseMessageResultType deviceResult,
            final OsgpException exception) {

        LOGGER.info("MessageType: {}. Handle update key response for device: {} for organisation: {}", messageType,
                deviceIdentification, organisationIdentification);

        ResponseMessageResultType result = ResponseMessageResultType.OK;
        OsgpException osgpException = exception;

        try {
            if (deviceResult == ResponseMessageResultType.NOT_OK || osgpException != null) {
                LOGGER.error("Device Response not ok.", osgpException);
                throw osgpException;
            }

            Ssld device = this.ssldRepository.findByDeviceIdentification(deviceIdentification);
            if (device == null) {
                // Device not found, create new device
                LOGGER.debug("Device [{}] does not exist, creating new device", deviceIdentification);
                device = new Ssld(deviceIdentification);
            }
            device.setPublicKeyPresent(true);
            this.ssldRepository.save(device);

            LOGGER.info("publicKey has been set for device: {} for organisation: {}", deviceIdentification,
                    organisationIdentification);

        } catch (final Exception e) {
            LOGGER.error("Unexpected Exception", e);
            result = ResponseMessageResultType.NOT_OK;
            osgpException = new TechnicalException(ComponentType.UNKNOWN, "Exception occurred while updating key", e);
        }

        final ResponseMessage responseMessage = ResponseMessage.newResponseMessageBuilder()
                .withCorrelationUid(correlationUid).withOrganisationIdentification(organisationIdentification)
                .withDeviceIdentification(deviceIdentification).withResult(result).withOsgpException(osgpException)
                .build();
        this.webServiceResponseMessageSender.send(responseMessage);
    }

    // === REVOKE KEY ===

    public void revokeKey(final String organisationIdentification, @Identification final String deviceIdentification,
            final String correlationUid, final String messageType) throws FunctionalException {

        LOGGER.info("MessageType: {}. Revoking key for device [{}] on behalf of organisation [{}]", messageType,
                deviceIdentification, organisationIdentification);

        try {
            this.organisationDomainService.searchOrganisation(organisationIdentification);
        } catch (final UnknownEntityException e) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.DOMAIN_ADMIN, e);
        }

        this.osgpCoreRequestMessageSender.send(
                new RequestMessage(correlationUid, organisationIdentification, deviceIdentification, null), messageType,
                null);

    }

    public void handleRevokeKeyResponse(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final String messageType, final ResponseMessageResultType deviceResult,
            final OsgpException exception) {

        LOGGER.info("MessageType: {}. Handle revoke key for device: {} for organisation: {}", messageType,
                deviceIdentification, organisationIdentification);

        ResponseMessageResultType result = ResponseMessageResultType.OK;
        OsgpException osgpException = exception;

        try {
            if (deviceResult == ResponseMessageResultType.NOT_OK || osgpException != null) {
                LOGGER.error("Device Response not ok.", osgpException);
                throw osgpException;
            }

            final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
            if (device == null) {
                throw new PlatformException(String.format("Device not found: %s", deviceIdentification));
            }

            final Ssld ssld = this.ssldRepository.findByDeviceIdentification(deviceIdentification);

            ssld.setPublicKeyPresent(false);
            this.ssldRepository.save(ssld);

            LOGGER.info("publicKey has been revoked for device: {} for organisation: {}", deviceIdentification,
                    organisationIdentification);

        } catch (final Exception e) {
            LOGGER.error("Unexpected Exception", e);
            result = ResponseMessageResultType.NOT_OK;
            osgpException = new TechnicalException(ComponentType.UNKNOWN, "Exception occurred while revoking key", e);
        }

        final ResponseMessage responseMessage = ResponseMessage.newResponseMessageBuilder()
                .withCorrelationUid(correlationUid).withOrganisationIdentification(organisationIdentification)
                .withDeviceIdentification(deviceIdentification).withResult(result).withOsgpException(osgpException)
                .build();
        this.webServiceResponseMessageSender.send(responseMessage);
    }
    
    public void handleActivateDeviceResponse(final String deviceIdentification, final String organisationIdentification,
            final String correlationUid, final String messageType, final ResponseMessageResultType deviceResult,
            final OsgpException exception, final RtuDeviceDto rtuDeviceDto) {

        LOGGER.info("MessageType: {}. Handle activate device response for device: {} for organisation: {}", messageType,
                deviceIdentification, organisationIdentification);

        ResponseMessageResultType result = ResponseMessageResultType.OK;
        OsgpException osgpException = exception;

        Ssld device = this.ssldRepository.findByDeviceIdentification(deviceIdentification);
        
        try {
            if (deviceResult == ResponseMessageResultType.NOT_OK || osgpException != null) {
            	device = this.ssldRepository.findByDeviceIdentification(deviceIdentification);
        		device.setDeviceLifecycleStatus(DeviceLifecycleStatus.UNDER_TEST);

        		this.ssldRepository.save(device);
            	
            	LOGGER.error("Device Response not ok.", osgpException);
                throw osgpException;
            }
            
            String ipAddress = rtuDeviceDto.getIp().trim();
			InetAddress networkAddress = InetAddress.getByName(ipAddress);
            device.updateRegistrationData(networkAddress, "RTU");
            device.setPublicKeyPresent(true);
            this.ssldRepository.save(device);
            
            List<Position> positions = new ArrayList<Position>();
            OrmRtuDevice rtuDevice = this.ormRtuDeviceRepository.findByDeviceIdentification(deviceIdentification);
            if (rtuDevice == null) {
            	rtuDevice = new OrmRtuDevice(deviceIdentification);
            } 		
            rtuDevice.setPositions(positions);
            
            for(PositionDto positionDto : rtuDeviceDto.getPositions()) {
            	positions.add(new Position(
            			positionDto.getNumPos(),
            			positionDto.getType() != null ? positionDto.getType() : null,
            			positionDto.getName() != null ? positionDto.getName() : null,
            			positionDto.getStatus() != null ? positionDto.getStatus() : null,
            			rtuDevice));
            }
            
            this.ormRtuDeviceRepository.save(rtuDevice);
            
            LOGGER.info("Device {} has been activated for organisation: {}", deviceIdentification,
                    organisationIdentification);

        } catch (final Exception e) {
            LOGGER.error("Unexpected Exception", e);
            result = ResponseMessageResultType.NOT_OK;
            osgpException = new TechnicalException(ComponentType.UNKNOWN, "Exception occurred while updating key", e);
        }

        final ResponseMessage responseMessage = ResponseMessage.newResponseMessageBuilder()
                .withCorrelationUid(correlationUid).withOrganisationIdentification(organisationIdentification)
                .withDeviceIdentification(deviceIdentification).withResult(result).withOsgpException(osgpException)
                .build();
        this.webServiceResponseMessageSender.send(responseMessage);
    }

}
