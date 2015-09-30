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

public class FindEventsQuery implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 150978792120024431L;

    private final EventLogCategory eventLogCategory;
    private final DateTime from;
    private final DateTime until;

    public FindEventsQuery(final EventLogCategory eventLogCategory, final DateTime from, final DateTime until) {
        this.eventLogCategory = eventLogCategory;
        this.from = from;
        this.until = until;
    }

    public EventLogCategory getEventLogCategory() {
        return this.eventLogCategory;
    }

    public DateTime getFrom() {
        return this.from;
    }

    public DateTime getUntil() {
        return this.until;
    }
}
