/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.mapping;

import static com.alliander.osgp.adapter.ws.smartmetering.application.mapping.MonitoringMapper.getMeterValue;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.AmrProfileStatusCode;
import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodType;
import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGas;
import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGasResponse;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.OsgpUnit;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReadsContainerGas;

public class PeriodicMeterReadsResponseGasConverter
        extends
        CustomConverter<PeriodicMeterReadsContainerGas, com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGasResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodicMeterReadsResponseGasConverter.class);

    @Override
    public PeriodicMeterReadsGasResponse convert(final PeriodicMeterReadsContainerGas source,
            final Type<? extends PeriodicMeterReadsGasResponse> destinationType) {
        final PeriodicMeterReadsGasResponse periodicMeterReadsResponse = new PeriodicMeterReadsGasResponse();
        periodicMeterReadsResponse.setPeriodType(PeriodType.valueOf(source.getPeriodType().name()));
        final List<PeriodicMeterReadsGas> periodicMeterReads = periodicMeterReadsResponse.getPeriodicMeterReadsGas();
        for (final com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReadsGas m : source
                .getMeterReadsGas()) {
            periodicMeterReads.add(this.convert(m, source.getOsgpUnit()));
        }
        return periodicMeterReadsResponse;
    }

    private PeriodicMeterReadsGas convert(
            final com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReadsGas source,
            final OsgpUnit osgpUnit) {
        final PeriodicMeterReadsGas meterReads = new PeriodicMeterReadsGas();
        final GregorianCalendar c = new GregorianCalendar();
        c.setTime(source.getLogTime());
        XMLGregorianCalendar convertedDate;
        try {
            convertedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (final DatatypeConfigurationException e) {
            LOGGER.error("JAXB mapping: An error occured while converting calendar types.", e);
            convertedDate = null;
        }
        final AmrProfileStatusCode amrProfileStatusCode = this.mapperFacade.map(source.getAmrProfileStatusCode(),
                AmrProfileStatusCode.class);

        meterReads.setLogTime(convertedDate);
        meterReads.setConsumption(getMeterValue(source.getConsumption(), osgpUnit));
        c.setTime(source.getCaptureTime());
        try {
            convertedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (final DatatypeConfigurationException e) {
            LOGGER.error("JAXB mapping: An error occured while converting calendar types.", e);
            convertedDate = null;
        }
        meterReads.setCaptureTime(convertedDate);
        meterReads.setAmrProfileStatusCode(amrProfileStatusCode);
        return meterReads;
    }

}
