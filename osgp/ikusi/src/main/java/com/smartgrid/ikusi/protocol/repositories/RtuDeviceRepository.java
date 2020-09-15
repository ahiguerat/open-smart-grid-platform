/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.smartgrid.ikusi.protocol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartgrid.ikusi.protocol.entities.OrmRtuDevice;

@Repository
public interface RtuDeviceRepository extends JpaRepository<OrmRtuDevice, Long> {

    OrmRtuDevice findByDeviceIdentification(String deviceIdentification);
	
}
