/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.smartgrid.ikusi.protocol.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Abstract base class for entities.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    // === FIELDS [START] ===

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1303732164728920566L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(nullable = false)
    private Date creationTime = new Date();

    @Column(nullable = false)
    private Date modificationTime = new Date();

    /**
     * @return the id
     */
    public final Long getId() {
        return this.id;
    }

    /**
     * @return the creation time
     */
    public final Date getCreationTime() {
        return (Date) this.creationTime.clone();
    }

    /**
     * @return the modification time
     */
    public final Date getModificationTime() {
        return (Date) this.modificationTime.clone();
    }

    /**
     * Method for actions to be taken before inserting.
     */
    @PrePersist
    private void prePersist() {
        final Date now = new Date();
        this.creationTime = now;
        this.modificationTime = now;
    }

    /**
     * Method for actions to be taken before updating.
     */
    @PreUpdate
    private void preUpdate() {
        this.modificationTime = new Date();
    }

    // === METHODS [END] ===
}
