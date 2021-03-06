/**
 * Copyright 2019 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.distributionautomation.config;

import org.opensmartgridplatform.cucumber.platform.distributionautomation.mocks.iec60870.Iec60870MockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Iec60870MockServerConfig {

    @Value("${iec60870.mock.networkaddress}")
    private String iec60870MockNetworkAddress;

    @Value("${iec60870.mock.connection.timeout:10_000}")
    private int connectionTimeout;

    @Value("${iec60870.mock.port}")
    private int port;

    @Bean
    public String iec60870MockNetworkAddress() {
        return this.iec60870MockNetworkAddress;
    }

    @Bean(destroyMethod = "stop", initMethod = "start")
    public Iec60870MockServer iec60870MockServer() {
        return new Iec60870MockServer(this.port, this.connectionTimeout);
    }
}
