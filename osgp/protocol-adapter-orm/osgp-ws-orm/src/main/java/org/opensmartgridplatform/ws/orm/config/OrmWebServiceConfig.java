/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.ws.orm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.WsdlDefinition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

@Configuration
public class OrmWebServiceConfig {

    private static final String UPDATE_SOFT_FINISHED_WSDL_PATH = "schemas/updatesoftfinished.xsd";

    private static final String UPDATE_SOFT_FINISHED_XSD_PATH = "UpdateSoftFinished.wsdl";
        
    @Bean(name = "UpdateSoftFinished")
    public WsdlDefinition firmwareManagementWsdl() {
        return new SimpleWsdl11Definition(new ClassPathResource(UPDATE_SOFT_FINISHED_WSDL_PATH));
    }

    @Bean(name = "updatesoftfinished")
    public SimpleXsdSchema firmwareManagementXsd() {
        return new SimpleXsdSchema(new ClassPathResource(UPDATE_SOFT_FINISHED_XSD_PATH));
    }
}
