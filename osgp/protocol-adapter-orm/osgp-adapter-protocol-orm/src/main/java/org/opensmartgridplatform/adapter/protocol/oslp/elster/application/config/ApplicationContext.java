 /* Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.oslp.elster.application.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;


import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.opensmartgridplatform.adapter.protocol.oslp.elster.device.FirmwareLocation;
import org.opensmartgridplatform.shared.application.config.AbstractConfig;
import org.opensmartgridplatform.shared.application.config.PagingSettings;
import org.opensmartgridplatform.adapter.ws.shared.db.application.config.WritablePersistenceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

/**
 * An application context Java configuration class.
 */
@Configuration
@EnableWs
@ComponentScan(basePackages = { "org.opensmartgridplatform.adapter.protocol.oslp.elster",
        "org.opensmartgridplatform.adapter.protocol.oslp.elster.ws.endpoints" })
@EnableTransactionManagement()
@Import({ MessagingConfig.class, OslpConfig.class, OslpPersistenceConfig.class, WritablePersistenceConfig.class })
@PropertySource("classpath:osgp-adapter-protocol-oslp-elster.properties")
@PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true)
@PropertySource(value = "file:${osgp/AdapterProtocolOslpElster/config}", ignoreResourceNotFound = true)
public class ApplicationContext extends AbstractConfig {

    private static final String PROPERTY_NAME_FIRMWARE_DOMAIN = "firmware.domain";
    private static final String PROPERTY_NAME_FIRMWARE_PATH = "firmware.path";
    private static final String PROPERTY_NAME_PAGING_MAXIMUM_PAGE_SIZE = "paging.maximum.pagesize";
    private static final String PROPERTY_NAME_PAGING_DEFAULT_PAGE_SIZE = "paging.default.pagesize";

    private static final String PROPERTY_NAME_LOCAL_TIME_ZONE_IDENTIFIER = "local.time.zone";
    
	private static final String PROPERTY_NAME_FTP_URL = "orm.ftp.url";
	private static final String PROPERTY_NAME_FTP_PORT = "orm.ftp.port";
	private static final String PROPERTY_NAME_FTP_LOGIN = "orm.ftp.login";
	private static final String PROPERTY_NAME_FTP_PASSWORD = "orm.ftp.password";
	private static final String PROPERTY_NAME_FTP_PATH = "orm.ftp.path";
	private static final String PROPERTY_NAME_RTU_TIME_INTERVAL = "orm.rtu.timeInterval";

    public ApplicationContext() {
        InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);
    }

    @Bean(name = "oslpPagingSettings")
    public PagingSettings pagingSettings() {
        return new PagingSettings(
                Integer.parseInt(this.environment.getRequiredProperty(PROPERTY_NAME_PAGING_MAXIMUM_PAGE_SIZE)),
                Integer.parseInt(this.environment.getRequiredProperty(PROPERTY_NAME_PAGING_DEFAULT_PAGE_SIZE)));
    }

    @Bean
    public FirmwareLocation firmwareLocation() {
        return new FirmwareLocation(this.environment.getProperty(PROPERTY_NAME_FIRMWARE_DOMAIN),
                this.environment.getProperty(PROPERTY_NAME_FIRMWARE_PATH));
    }

    // === Time zone config ===

    @Bean
    public String localTimeZoneIdentifier() {
        return this.environment.getRequiredProperty(PROPERTY_NAME_LOCAL_TIME_ZONE_IDENTIFIER);
    }

    @Bean
    public DateTimeZone localTimeZone() {
        return DateTimeZone.forID(this.localTimeZoneIdentifier());
    }

    @Bean
    public Integer timeZoneOffsetMinutes() {
        return this.localTimeZone().getStandardOffset(new DateTime().getMillis()) / DateTimeConstants.MILLIS_PER_MINUTE;
    }
	@Bean("soapMessageFactory")
	public MessageFactory soapMessageFactory() throws SOAPException {
		return MessageFactory.newInstance();
	}

	@Bean
	public SaajSoapMessageFactory saajSoapMessageFactory(@Qualifier("soapMessageFactory")MessageFactory messageFactory) {
		SaajSoapMessageFactory soapMessageFactory = new SaajSoapMessageFactory(messageFactory);
		soapMessageFactory.afterPropertiesSet();
		return soapMessageFactory;
	}
	
	@Bean
	public SaajSoapMessageFactory messageFactory() {
	    SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
	    messageFactory.setSoapVersion(SoapVersion.SOAP_12);
	    return messageFactory;
	}

	@Bean
	public Jaxb2Marshaller getStationInfoMarshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath("org.opensmartgridplatform.adapter.ws.schema.orm.getstationinfo");
        
        return marshaller;
	}

	@Bean
	public Jaxb2Marshaller updateSoftMarshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath("org.opensmartgridplatform.adapter.ws.schema.orm.updatesoft");
        
        return marshaller;
	}
	
	@Bean
	public Jaxb2Marshaller getEventsMarshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath("org.opensmartgridplatform.adapter.ws.schema.orm.getevents");
        
        return marshaller;
	}
	
	@Bean
	public Jaxb2Marshaller updateSoftFinishedMarshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath("org.opensmartgridplatform.adapter.ws.schema.orm.updatesoftfinished");
        
        return marshaller;
	}
	
	@Bean
	public WebServiceTemplate getEventsWSTemplate(SaajSoapMessageFactory messageFactory)
			throws Exception {
		
		Jaxb2Marshaller marshaller = this.getEventsMarshaller();

		WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);

		marshaller.afterPropertiesSet();

		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		webServiceTemplate.afterPropertiesSet();

		return webServiceTemplate;
	}

	@Bean
	public WebServiceTemplate getStationWSTemplate(SaajSoapMessageFactory messageFactory)
			throws Exception {
		
		Jaxb2Marshaller marshaller = this.getStationInfoMarshaller();

		WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);

		marshaller.afterPropertiesSet();

		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		webServiceTemplate.afterPropertiesSet();

		return webServiceTemplate;
	}

	@Bean
	public WebServiceTemplate updateSoftWSTemplate(SaajSoapMessageFactory messageFactory) throws Exception {

		Jaxb2Marshaller marshaller = this.updateSoftMarshaller();
		
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);

		marshaller.afterPropertiesSet();

		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		webServiceTemplate.afterPropertiesSet();

		return webServiceTemplate;
	}

	@Bean
    public MarshallingPayloadMethodProcessor updateSoftFinishedMarshallingPayloadMethodProcessor() {
//        LOGGER.debug("Creating Device Installation Marshalling Payload Method Processor Bean");

        return new MarshallingPayloadMethodProcessor(this.updateSoftFinishedMarshaller(),
                this.updateSoftFinishedMarshaller());
    }

	@Bean
    public DefaultMethodEndpointAdapter defaultMethodEndpointAdapter() {
//        LOGGER.debug("Creating Default Method Endpoint Adapter Bean");

        final DefaultMethodEndpointAdapter defaultMethodEndpointAdapter = new DefaultMethodEndpointAdapter();

        final List<MethodArgumentResolver> methodArgumentResolvers = new ArrayList<>();

        methodArgumentResolvers.add(this.updateSoftFinishedMarshallingPayloadMethodProcessor());
      
        defaultMethodEndpointAdapter.setMethodArgumentResolvers(methodArgumentResolvers);

        final List<MethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<>();

        methodReturnValueHandlers.add(this.updateSoftFinishedMarshallingPayloadMethodProcessor());

        defaultMethodEndpointAdapter.setMethodReturnValueHandlers(methodReturnValueHandlers);

        return defaultMethodEndpointAdapter;
    }

	@Bean
	public String ftpUrl() {
		return this.environment.getRequiredProperty(PROPERTY_NAME_FTP_URL);
	}

	@Bean
	public String ftpPort() {
		return this.environment.getRequiredProperty(PROPERTY_NAME_FTP_PORT);
	}

	@Bean
	public String ftpLogin() {
		return this.environment.getRequiredProperty(PROPERTY_NAME_FTP_LOGIN);
	}

	@Bean
	public String ftpPassword() {
		return this.environment.getRequiredProperty(PROPERTY_NAME_FTP_PASSWORD);
	}

	@Bean
	public String ftpPath() {
		return this.environment.getRequiredProperty(PROPERTY_NAME_FTP_PATH);
	}
	
	@Bean
	public String timeInterval() {
		return this.environment.getRequiredProperty(PROPERTY_NAME_RTU_TIME_INTERVAL);
	}
}
