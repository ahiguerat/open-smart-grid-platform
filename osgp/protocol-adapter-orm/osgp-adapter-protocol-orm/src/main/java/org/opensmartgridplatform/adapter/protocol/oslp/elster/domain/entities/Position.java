package org.opensmartgridplatform.adapter.protocol.oslp.elster.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.opensmartgridplatform.shared.domain.entities.AbstractEntity;

@Entity
public class Position extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column
	private String numPos;

	@Column
	private String type;

	@Column
	private String name;

	@Column
	private String status;
	
	@ManyToOne
    @JoinColumn(name = "rtu_device_id", nullable = false, updatable = false, insertable = true)
    private RtuDevice rtuDevice;

	public Position() {
	}

	public Position(String numPos, String type, String name, String status, RtuDevice rtuDevice) {
		super();
		this.numPos = numPos;
		this.type = type;
		this.name = name;
		this.status = status;
		this.rtuDevice = rtuDevice;
	}

	public String getNumPos() {
		return numPos;
	}

	public void setNumPos(String numPos) {
		this.numPos = numPos;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}