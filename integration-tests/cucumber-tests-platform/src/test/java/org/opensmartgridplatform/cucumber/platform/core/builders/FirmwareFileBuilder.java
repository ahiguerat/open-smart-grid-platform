/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.core.builders;

import java.util.Map;

import org.opensmartgridplatform.cucumber.platform.PlatformDefaults;
import org.opensmartgridplatform.cucumber.platform.PlatformKeys;
import org.opensmartgridplatform.domain.core.entities.DeviceModel;
import org.opensmartgridplatform.domain.core.entities.FirmwareFile;
import org.opensmartgridplatform.domain.core.repositories.FirmwareModuleRepository;
import org.opensmartgridplatform.domain.core.valueobjects.FirmwareModuleData;

public class FirmwareFileBuilder implements CucumberBuilder<FirmwareFile> {

    private DeviceModel deviceModel;
    private String filename;
    private String description = PlatformDefaults.FIRMWARE_DESCRIPTION;
    private boolean pushToNewDevices = PlatformDefaults.FIRMWARE_PUSH_TO_NEW_DEVICE;
    private String moduleVersionComm = PlatformDefaults.FIRMWARE_MODULE_VERSION_COMM;
    private String moduleVersionFunc = PlatformDefaults.FIRMWARE_MODULE_VERSION_FUNC;
    private String moduleVersionMa = PlatformDefaults.FIRMWARE_MODULE_VERSION_MA;
    private String moduleVersionMbus = PlatformDefaults.FIRMWARE_MODULE_VERSION_MBUS;
    private String moduleVersionSec = PlatformDefaults.FIRMWARE_MODULE_VERSION_SEC;
    private String moduleVersionMBusDriverActive = PlatformDefaults.FIRMWARE_MODULE_VERSION_M_BUS_DRIVER_ACTIVE;

    
    // RTU modules
    private String moduleVersionXmlInt = PlatformDefaults.FIRMWARE_MODULE_VERSION_XMLINT;
    private String moduleVersionXml2ccp = PlatformDefaults.FIRMWARE_MODULE_VERSION_XML2CCP;
    private String moduleVersionLibmmslite = PlatformDefaults.FIRMWARE_MODULE_VERSION_LIBMMSLITE;
    private String moduleVersionEkorccp = PlatformDefaults.FIRMWARE_MODULE_VERSION_EKORCCP;
    private String moduleVersionDimxccp = PlatformDefaults.FIRMWARE_MODULE_VERSION_DIMXCCP;
    private String moduleVersionRtuschemas = PlatformDefaults.FIRMWARE_MODULE_VERSION_RTUSCHEMAS;
    private String moduleVersionLocaltime = PlatformDefaults.FIRMWARE_MODULE_VERSION_LOCALTIME;
    private String moduleVersionLibxsdset = PlatformDefaults.FIRMWARE_MODULE_VERSION_LIBXSDSET;
    private String moduleVersionFreedisk = PlatformDefaults.FIRMWARE_MODULE_VERSION_FREEDISK;
    private String moduleVersionEkorrtuws = PlatformDefaults.FIRMWARE_MODULE_VERSION_EKORRTUWS;
    private String moduleVersionEkorWeb = PlatformDefaults.FIRMWARE_MODULE_VERSION_EKORWEB;
    private String moduleVersionCcpC = PlatformDefaults.FIRMWARE_MODULE_VERSION_CCPC;

    private String moduleVersionDarmccp = PlatformDefaults.FIRMWARE_MODULE_VERSION_DARMCCP;
    private String moduleVersionExpect = PlatformDefaults.FIRMWARE_MODULE_VERSION_EXPECT;
    private String moduleVersionOpenssh = PlatformDefaults.FIRMWARE_MODULE_VERSION_OPENSSH;
    private String moduleVersionOpenssl = PlatformDefaults.FIRMWARE_MODULE_VERSION_OPENSSL;
    private String moduleVersionProftpd = PlatformDefaults.FIRMWARE_MODULE_VERSION_PROFTPD;
    private String moduleVersionTcpdump = PlatformDefaults.FIRMWARE_MODULE_VERSION_TCPDUMP;

    private byte file[];    
    private String hash;

    public FirmwareFileBuilder withDeviceModel(final DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
        return this;
    }

    public FirmwareFileBuilder withFilename(final String filename) {
        this.filename = filename;
        return this;
    }

    public FirmwareFileBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public FirmwareFileBuilder withPushToNewDevices(final boolean pushToNewDevices) {
        this.pushToNewDevices = pushToNewDevices;
        return this;
    }

    public FirmwareFileBuilder withModuleVersionComm(final String moduleVersionComm) {
        this.moduleVersionComm = moduleVersionComm;
        return this;
    }

    public FirmwareFileBuilder withModuleVersionFunc(final String moduleVersionFunc) {
        this.moduleVersionFunc = moduleVersionFunc;
        return this;
    }

    public FirmwareFileBuilder withModuleVersionMa(final String moduleVersionMa) {
        this.moduleVersionMa = moduleVersionMa;
        return this;
    }

    public FirmwareFileBuilder withModuleVersionMbus(final String moduleVersionMbus) {
        this.moduleVersionMbus = moduleVersionMbus;
        return this;
    }

    public FirmwareFileBuilder withModuleVersionSec(final String moduleVersionSec) {
        this.moduleVersionSec = moduleVersionSec;
        return this;
    }

    public FirmwareFileBuilder withFile(final byte[] file) {
        this.file = file;
        return this;
    }

    public FirmwareFileBuilder withHash(final String hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public FirmwareFile build() {
        throw new UnsupportedOperationException(
                "Firmware module version configuration model in test builders needs to be made more generic. For now call: build(firmwareModuleRepository, isForSmartMeters)");
    }

    public FirmwareFile build(final FirmwareModuleRepository firmwareModuleRepository, final boolean isForSmartMeters) {
        final FirmwareFile.Builder firmwareFileBuilder = new FirmwareFile.Builder();
        firmwareFileBuilder.withFilename(this.filename)
                .withDescription(this.description)
                .withPushToNewDevices(this.pushToNewDevices)
                .withFile(this.file)
                .withHash(this.hash);
        final FirmwareFile firmwareFile = firmwareFileBuilder.build();
        if (this.deviceModel != null) {
            firmwareFile.addDeviceModel(this.deviceModel);
        }
        firmwareFile.updateFirmwareModuleData(new FirmwareModuleData(this.moduleVersionComm, this.moduleVersionFunc,
                this.moduleVersionMa, this.moduleVersionMbus, this.moduleVersionSec, this.moduleVersionMBusDriverActive, this.moduleVersionXmlInt, this.moduleVersionXml2ccp, this.moduleVersionLibmmslite,
                this.moduleVersionEkorccp, this.moduleVersionDimxccp, this.moduleVersionRtuschemas, this.moduleVersionLocaltime,
                this.moduleVersionLibxsdset, this.moduleVersionFreedisk, this.moduleVersionEkorrtuws, this.moduleVersionEkorWeb, this.moduleVersionCcpC,
                this.moduleVersionDarmccp, this.moduleVersionExpect, this.moduleVersionOpenssh, this.moduleVersionOpenssl,
                this.moduleVersionProftpd, this.moduleVersionTcpdump)
        		           .getVersionsByModule(firmwareModuleRepository, isForSmartMeters));
        return firmwareFile;
    }

    @Override
    public FirmwareFileBuilder withSettings(final Map<String, String> inputSettings) {
        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_FILE_FILENAME)) {
            this.withFilename(inputSettings.get(PlatformKeys.FIRMWARE_FILE_FILENAME));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_DESCRIPTION)) {
            this.withDescription(inputSettings.get(PlatformKeys.FIRMWARE_DESCRIPTION));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_PUSH_TO_NEW_DEVICES)) {
            this.withPushToNewDevices(
                    Boolean.parseBoolean(inputSettings.get(PlatformKeys.FIRMWARE_PUSH_TO_NEW_DEVICES)));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_MODULE_VERSION_COMM)) {
            this.withModuleVersionComm(inputSettings.get(PlatformKeys.FIRMWARE_MODULE_VERSION_COMM));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_MODULE_VERSION_FUNC)) {
            this.withModuleVersionFunc(inputSettings.get(PlatformKeys.FIRMWARE_MODULE_VERSION_FUNC));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_MODULE_VERSION_MA)) {
            this.withModuleVersionMa(inputSettings.get(PlatformKeys.FIRMWARE_MODULE_VERSION_MA));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_MODULE_VERSION_MBUS)) {
            this.withModuleVersionMbus(inputSettings.get(PlatformKeys.FIRMWARE_MODULE_VERSION_MBUS));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_MODULE_VERSION_SEC)) {
            this.withModuleVersionSec(inputSettings.get(PlatformKeys.FIRMWARE_MODULE_VERSION_SEC));
        }

        if (inputSettings.containsKey(PlatformKeys.FIRMWARE_HASH)) {
            this.withHash(inputSettings.get(PlatformKeys.FIRMWARE_HASH));
        }

        return this;
    }

}
