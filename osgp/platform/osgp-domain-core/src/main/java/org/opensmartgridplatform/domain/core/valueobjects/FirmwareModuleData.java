/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.opensmartgridplatform.domain.core.entities.FirmwareModule;
import org.opensmartgridplatform.domain.core.repositories.FirmwareModuleRepository;

public class FirmwareModuleData implements Serializable {

    private static final long serialVersionUID = 3479817852183883103L;

    /**
     * Description of the FirmwareModule for which the module version in a
     * FirmwareFile should equal {@link #moduleVersionComm}.
     */
    public static final String MODULE_DESCRIPTION_COMM = "communication_module_active_firmware";
    /**
     * Description of the FirmwareModule for which the module version in a
     * FirmwareFile should equal {@link #moduleVersionFunc} with devices other
     * than smart meters.
     */
    public static final String MODULE_DESCRIPTION_FUNC = "functional";
    /**
     * Description of the FirmwareModule for which the module version in a
     * FirmwareFile should equal {@link #moduleVersionFunc} with smart meter
     * devices.
     */
    public static final String MODULE_DESCRIPTION_FUNC_SMART_METERING = "active_firmware";
    /**
     * Description of the FirmwareModule for which the module version in a
     * FirmwareFile should equal {@link #moduleVersionMa}.
     */
    public static final String MODULE_DESCRIPTION_MA = "module_active_firmware";
    /**
     * Description of the FirmwareModule for which the module version in a
     * FirmwareFile should equal {@link #moduleVersionMbus}.
     */
    public static final String MODULE_DESCRIPTION_MBUS = "m_bus";
    /**
     * Description of the FirmwareModule for which the module version in a
     * FirmwareFile should equal {@link #moduleVersionSec}.
     */
    public static final String MODULE_DESCRIPTION_SEC = "security";
    /**
     * Description of the FirmwareModule for which the module version in a
     * FirmwareFile should equal {@link #moduleVersionMBusDriverActive}.
     */
    public static final String MODULE_DESCRIPTION_MBUS_DRIVER_ACTIVE = "m_bus_driver_active_firmware";

    // RTU modules
    public static final String MODULE_DESCRIPTION_XMLINT = "xmllint";
    public static final String MODULE_DESCRIPTION_XML2CCP = "xml2ccp";
    public static final String MODULE_DESCRIPTION_LIBMMSLITE = "libmmslite";
    public static final String MODULE_DESCRIPTION_EKORCCP = "ekorccp";
    public static final String MODULE_DESCRIPTION_DIMXCCP = "dimxccp";
    public static final String MODULE_DESCRIPTION_RTUSCHEMAS = "rtuschemas";
    public static final String MODULE_DESCRIPTION_LOCALTIME = "localtime";
    public static final String MODULE_DESCRIPTION_LIBXSDSET = "libxsdset";
    public static final String MODULE_DESCRIPTION_FREEDISK = "freedisk";
    public static final String MODULE_DESCRIPTION_EKORRTUWS = "ekorrtuws";
    public static final String MODULE_DESCRIPTION_EKORWEB = "ekorWeb";
    public static final String MODULE_DESCRIPTION_CCPC = "CcpC";
    
    public static final String MODULE_DESCRIPTION_DARMCCP = "darmccp";
    public static final String MODULE_DESCRIPTION_EXPECT = "expect";
    public static final String MODULE_DESCRIPTION_OPENSSH = "openssh";
    public static final String MODULE_DESCRIPTION_OPENSSL = "openssl";
    public static final String MODULE_DESCRIPTION_PROFTPD = "proftpd";
    public static final String MODULE_DESCRIPTION_TCPDUMP = "tcpdump";
    
    private final String moduleVersionComm;
    private final String moduleVersionFunc;
    private final String moduleVersionMa;
    private final String moduleVersionMbus;
    private final String moduleVersionSec;
    private final String moduleVersionMBusDriverActive;
    
    // RTU modules
    private final String moduleVersionXmllint;
    private final String moduleVersionXml2Ccp;
    private final String moduleVersionLibmmslite;
    private final String moduleVersionEkorccp;
    private final String moduleVersionDimxccp;
    private final String moduleVersionRtuschemas;
    private final String moduleVersionLocaltime;
    private final String moduleVersionLibxsdset;
    private final String moduleVersionFreedisk;
    private final String moduleVersionEkorrtuws;
    private final String moduleVersionEkorWeb;
    private final String moduleVersionCcpC;
    
    private final String moduleVersionDarmccp;
    private final String moduleVersionExpect;
    private final String moduleVersionOpenssh;
    private final String moduleVersionOpenssl;
    private final String moduleVersionProftpd;
    private final String moduleVersionTcpdump;

    public FirmwareModuleData(final String moduleVersionComm, final String moduleVersionFunc,
            final String moduleVersionMa, final String moduleVersionMbus, final String moduleVersionSec,
            final String moduleVersionMBusDriverActive, final String moduleVersionXmllint, final String moduleVersionXml2Ccp,
            final String moduleVersionLibmmslite, final String moduleVersionEkorccp, final String moduleVersionDimxccp,
            final String moduleVersionRtuschemas, final String moduleVersionLocaltime, final String moduleVersionLibxsdset,
            final String moduleVersionFreedisk, final String moduleVersionEkorrtuws, final String moduleVersionEkorWeb,
            final String moduleVersionCcpC, final String moduleVersionDarmccp, final String moduleVersionExpect,
            final String moduleVersionOpenssh, final String moduleVersionOpenssl, final String moduleVersionProftpd,
            final String moduleVersionTcpdump) {
    	
        this.moduleVersionComm = moduleVersionComm;
        this.moduleVersionFunc = moduleVersionFunc;
        this.moduleVersionMa = moduleVersionMa;
        this.moduleVersionMbus = moduleVersionMbus;
        this.moduleVersionSec = moduleVersionSec;
        this.moduleVersionMBusDriverActive = moduleVersionMBusDriverActive;
        
        // RTU modules
        this.moduleVersionXmllint = moduleVersionXmllint;
        this.moduleVersionXml2Ccp = moduleVersionXml2Ccp;
        this.moduleVersionLibmmslite = moduleVersionLibmmslite;
        this.moduleVersionEkorccp = moduleVersionEkorccp;
        this.moduleVersionDimxccp = moduleVersionDimxccp;
        this.moduleVersionRtuschemas = moduleVersionRtuschemas;
        this.moduleVersionLocaltime = moduleVersionLocaltime;
        this.moduleVersionLibxsdset = moduleVersionLibxsdset;
        this.moduleVersionFreedisk = moduleVersionFreedisk;
        this.moduleVersionEkorrtuws = moduleVersionEkorrtuws;
        this.moduleVersionEkorWeb = moduleVersionEkorWeb;
        this.moduleVersionCcpC = moduleVersionCcpC;
        
        this.moduleVersionDarmccp = moduleVersionDarmccp;
        this.moduleVersionExpect = moduleVersionExpect;
        this.moduleVersionOpenssh = moduleVersionOpenssh;
        this.moduleVersionOpenssl = moduleVersionOpenssl;
        this.moduleVersionProftpd = moduleVersionProftpd;
        this.moduleVersionTcpdump = moduleVersionTcpdump;
    }

    public String getModuleVersionComm() {
        return this.moduleVersionComm;
    }

    public String getModuleVersionFunc() {
        return this.moduleVersionFunc;
    }

    public String getModuleVersionMa() {
        return this.moduleVersionMa;
    }

    public String getModuleVersionMbus() {
        return this.moduleVersionMbus;
    }

    public String getModuleVersionSec() {
        return this.moduleVersionSec;
    }

    public String getModuleVersionMBusDriverActive() {
        return this.moduleVersionMBusDriverActive;
    }

	public String getModuleVersionXmllint() {
		return moduleVersionXmllint;
	}

	public String getModuleVersionXml2Ccp() {
		return moduleVersionXml2Ccp;
	}

	public String getModuleVersionLibmmslite() {
		return moduleVersionLibmmslite;
	}

	public String getModuleVersionEkorccp() {
		return moduleVersionEkorccp;
	}

	public String getModuleVersionDimxccp() {
		return moduleVersionDimxccp;
	}

	public String getModuleVersionRtuschemas() {
		return moduleVersionRtuschemas;
	}

	public String getModuleVersionLocaltime() {
		return moduleVersionLocaltime;
	}

	public String getModuleVersionLibxsdset() {
		return moduleVersionLibxsdset;
	}

	public String getModuleVersionFreedisk() {
		return moduleVersionFreedisk;
	}

	public String getModuleVersionEkorrtuws() {
		return moduleVersionEkorrtuws;
	}

	public String getModuleVersionEkorWeb() {
		return moduleVersionEkorWeb;
	}

	public String getModuleVersionCcpC() {
		return moduleVersionCcpC;
	}

	public String getModuleVersionDarmccp() {
		return moduleVersionDarmccp;
	}

	public String getModuleVersionExpect() {
		return moduleVersionExpect;
	}

	public String getModuleVersionOpenssh() {
		return moduleVersionOpenssh;
	}

	public String getModuleVersionOpenssl() {
		return moduleVersionOpenssl;
	}

	public String getModuleVersionProftpd() {
		return moduleVersionProftpd;
	}

	public String getModuleVersionTcpdump() {
		return moduleVersionTcpdump;
	}

	/**
     * Returns the FirmwareModuleData as a map of FirmwareModule to version
     * String.
     * <p>
     * This should probably be a temporary workaround, until the use of the
     * FirmwareModuleData is replaced by some other code that better matches the
     * generic firmware module set up that does not assume a fixed number of
     * module types.
     *
     * @param firmwareModuleRepository
     *            a repository to be able to retrieve the known firmware modules
     *            from the FirmwareModuleData from the database.
     * @param isForSmartMeters
     *            if {@code true} {@link #moduleVersionFunc} is mapped to the
     *            {@value #MODULE_DESCRIPTION_FUNC_SMART_METERING} firmware
     *            module; otherwise it is mapped to the
     *            {@value #MODULE_DESCRIPTION_FUNC} firmware module.
     * @return firmware module versions by module.
     */
    public Map<FirmwareModule, String> getVersionsByModule(final FirmwareModuleRepository firmwareModuleRepository,
            final boolean isForSmartMeters) {

        final Map<FirmwareModule, String> versionsByModule = new TreeMap<>();

        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionComm,
                MODULE_DESCRIPTION_COMM);
        if (isForSmartMeters) {
            this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionFunc,
                    MODULE_DESCRIPTION_FUNC_SMART_METERING);
        } else {
            this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionFunc,
                    MODULE_DESCRIPTION_FUNC);
        }
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionMa,
                MODULE_DESCRIPTION_MA);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionMbus,
                MODULE_DESCRIPTION_MBUS);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionSec,
                MODULE_DESCRIPTION_SEC);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository,
                this.moduleVersionMBusDriverActive, MODULE_DESCRIPTION_MBUS_DRIVER_ACTIVE);
        
        // RTU modules
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionXmllint,
                MODULE_DESCRIPTION_XMLINT);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionXml2Ccp,
                MODULE_DESCRIPTION_XML2CCP);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionLibmmslite,
                MODULE_DESCRIPTION_LIBMMSLITE);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionEkorccp,
                MODULE_DESCRIPTION_EKORCCP);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionDimxccp,
                MODULE_DESCRIPTION_DIMXCCP);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionRtuschemas,
                MODULE_DESCRIPTION_RTUSCHEMAS);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionLocaltime,
                MODULE_DESCRIPTION_LOCALTIME);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionLibxsdset,
                MODULE_DESCRIPTION_LIBXSDSET);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionFreedisk,
                MODULE_DESCRIPTION_FREEDISK);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionEkorrtuws,
                MODULE_DESCRIPTION_EKORRTUWS);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionEkorWeb,
                MODULE_DESCRIPTION_EKORWEB);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionCcpC,
                MODULE_DESCRIPTION_CCPC);
        
        // ARM modules
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionDarmccp,
                MODULE_DESCRIPTION_DARMCCP);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionExpect,
                MODULE_DESCRIPTION_EXPECT);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionOpenssh,
                MODULE_DESCRIPTION_OPENSSH);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionOpenssl,
                MODULE_DESCRIPTION_OPENSSL);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionProftpd,
                MODULE_DESCRIPTION_PROFTPD);
        this.addVersionForModuleIfNonBlank(versionsByModule, firmwareModuleRepository, this.moduleVersionTcpdump,
                MODULE_DESCRIPTION_TCPDUMP);
        
        return versionsByModule;
    }

    private void addVersionForModuleIfNonBlank(final Map<FirmwareModule, String> versionsByModule,
            final FirmwareModuleRepository firmwareModuleRepository, final String moduleVersion,
            final String moduleDescription) {
        if (!StringUtils.isEmpty(moduleVersion)) {
            versionsByModule.put(firmwareModuleRepository.findByDescriptionIgnoreCase(moduleDescription),
                    moduleVersion);
        }
    }

    @Override
    public String toString() {
        return "FirmwareModuleData [moduleVersionComm=" + this.moduleVersionComm + ", moduleVersionFunc="
                + this.moduleVersionFunc + ", moduleVersionMa=" + this.moduleVersionMa + ", moduleVersionMbus="
                + this.moduleVersionMbus + ", moduleVersionSec=" + this.moduleVersionSec
                + ", moduleVersionMBusDriverActive=" + this.moduleVersionMBusDriverActive + "]";
    }

}
