/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;

import org.joda.time.DateTime;

public class Event implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4482313912422705642L;
    private DateTime timestamp;
    private Integer eventCode;

    public Event(final DateTime timestamp, final Integer eventCode) {
        this.timestamp = timestamp;
        this.eventCode = eventCode;
    }

    public DateTime getTimestamp() {
        return this.timestamp;
    }

    public Integer getEventCode() {
        return this.eventCode;
    }

}
