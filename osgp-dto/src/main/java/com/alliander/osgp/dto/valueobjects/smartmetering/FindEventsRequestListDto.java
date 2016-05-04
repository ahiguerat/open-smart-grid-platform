/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.dto.valueobjects.smartmetering;

import java.io.Serializable;
import java.util.List;

public class FindEventsRequestListDto implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1917863566442592894L;

    private List<FindEventsRequestDataDto> findEventsQueryList;

    public FindEventsRequestListDto(final List<FindEventsRequestDataDto> findEventsQueryList) {
        this.findEventsQueryList = findEventsQueryList;
    }

    public List<FindEventsRequestDataDto> getFindEventsQueryList() {
        return this.findEventsQueryList;
    }
}
